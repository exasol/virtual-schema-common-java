package com.exasol.adapter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.AdapterRequest;
import com.exasol.adapter.request.LoggingConfiguration;
import com.exasol.adapter.request.parser.RequestParser;
import com.exasol.errorreporting.ExaError;
import com.exasol.logging.RemoteLogManager;
import com.exasol.logging.VersionCollector;

/**
 * This class is the main entry point for calls to a Virtual Schema. It sets up the application and delegate the control
 * to the {@link AdapterCallExecutor}.
 */
public final class RequestDispatcher {
    private static final Logger LOGGER = Logger.getLogger(RequestDispatcher.class.getName());

    private RequestDispatcher() {
        // Not instantiable
    }

    /**
     * Main entry point for all Virtual Schema Adapter requests issued by the Exasol database.
     *
     * @param metadata   metadata for the context in which the adapter exists (e.g. the schema into which it is
     *                   installed)
     * @param rawRequest request issued in the call to the Virtual Schema Adapter
     * @return response resulting from the adapter call
     * @throws AdapterException in case the request type is not recognized
     */
    public static String adapterCall(final ExaMetadata metadata, final String rawRequest) throws AdapterException {
        try {
            return processAdapterCall(metadata, rawRequest);
        } catch (final Exception exception) {
            LOGGER.severe(exception::getMessage);
            LOGGER.log(Level.FINE, "Stack trace:", exception);
            throw exception;
        }
    }

    @SuppressWarnings("java:S106") // we need stdout since LOGGER is not yet available
    private static String processAdapterCall(final ExaMetadata metadata, final String rawRequest)
            throws AdapterException {
        final AdapterRequest adapterRequest;
        try {
            adapterRequest = parseRequest(rawRequest);
            configureAdapterLoggingAccordingToRequestSettings(adapterRequest);
        } catch (final RuntimeException exception) {
            System.out.println("Raw JSON request:\n" + rawRequest);
            throw exception;
        }
        logVersionInformation();
        logRawRequest(rawRequest);
        final AdapterCallExecutor adapterCallExecutor = getAdapterCallExecutor();
        final String response = adapterCallExecutor.executeAdapterCall(adapterRequest, metadata);
        logRawResponse(response);
        return response;
    }

    private static void logVersionInformation() {
        final VersionCollector versionCollector = new VersionCollector();
        LOGGER.info("Loaded versions: virtual-schema-common-java " + versionCollector.getVersionNumber());
    }

    private static void logRawRequest(final String rawRequest) {
        LOGGER.finer(() -> "Raw JSON request:\n" + rawRequest);
    }

    private static void logRawResponse(final String response) {
        LOGGER.finer(() -> "Raw JSON response: '" + response + "'");
    }

    private static AdapterRequest parseRequest(final String rawRequest) {
        return RequestParser.create().parse(rawRequest);
    }

    private static void configureAdapterLoggingAccordingToRequestSettings(final AdapterRequest request) {
        final LoggingConfiguration configuration = LoggingConfiguration
                .parseFromProperties(request.getSchemaMetadataInfo().getProperties());
        final RemoteLogManager remoteLogManager = new RemoteLogManager();
        if (configuration.isRemoteLoggingConfigured()) {
            remoteLogManager.setupRemoteLogger(configuration.getRemoteLoggingHost(),
                    configuration.getRemoteLoggingPort(), configuration.getLogLevel());
        } else {
            remoteLogManager.setupConsoleLogger(configuration.getLogLevel());
        }
    }

    private static AdapterCallExecutor getAdapterCallExecutor() {
        return new AdapterCallExecutor(getVirtualSchemaAdapter());
    }

    private static VirtualSchemaAdapter getVirtualSchemaAdapter() {
        return getAdapterFactory().createAdapter();
    }

    private static AdapterFactory getAdapterFactory() {
        final ServiceLoader<AdapterFactory> adapterFactoryLoader = ServiceLoader.load(AdapterFactory.class);
        final Optional<AdapterFactory> adapterFactory = adapterFactoryLoader.findFirst();
        return adapterFactory.orElseThrow(() -> new NoSuchElementException(
                ExaError.messageBuilder("E-VSCOMJAVA-29").message("No AdapterFactory was found.").toString()));
    }
}
