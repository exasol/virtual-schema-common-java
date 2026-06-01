package com.exasol.adapter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.request.parser.RequestParser;
import com.exasol.errorreporting.ExaError;
import com.exasol.logging.RemoteLogManager;
import com.exasol.logging.VersionCollector;
import com.exasol.telemetry.TelemetryClient;
import com.exasol.telemetry.TelemetryConfig;

/**
 * This class is the main entry point for calls to a Virtual Schema. It sets up the application and delegate the control
 * to the {@link AdapterCallExecutor}.
 */
public final class RequestDispatcher {
    private static final Logger LOGGER = Logger.getLogger(RequestDispatcher.class.getName());
    private static final String VSCOMMON_JAVA_VERSION_PROPERTIES = "META-INF/maven/com.exasol/virtual-schema-common-java/pom.properties";

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
    @SuppressWarnings("java:S2139") // Re-throwing the exception is intentional, we only want to log it.
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
        final AdapterFactory adapterFactory = getAdapterFactory();
        try (TelemetryClient telemetryClient = createTelemetryClient(adapterFactory, adapterRequest)) {
            final AdapterContext context = new AdapterContext(telemetryClient);
            final VirtualSchemaAdapter virtualSchemaAdapter = adapterFactory.createAdapter(context);
            final AdapterCallExecutor adapterCallExecutor = new AdapterCallExecutor(virtualSchemaAdapter, telemetryClient);
            final String response = adapterCallExecutor.executeAdapterCall(adapterRequest, metadata);
            logRawResponse(response);
            return response;
        }
    }

    // Visible for testing
    static TelemetryClient createTelemetryClient(final AdapterFactory factory, final AdapterRequest adapterRequest) {
        final AdapterTelemetryConfiguration adapterConfig = AdapterTelemetryConfiguration
                .parseFromProperties(adapterRequest.getSchemaMetadataInfo().getProperties());
        final TelemetryConfig.Builder configBuilder = TelemetryConfig.builder(factory.getAdapterProjectShortTag(), factory.getAdapterVersion());
        if (adapterConfig.isTelemetryDisabled()) {
            configBuilder.disableTracking();
        }
        return TelemetryClient.create(configBuilder.build());
    }

    private static void logVersionInformation() {
        final VersionCollector versionCollector = new VersionCollector(VSCOMMON_JAVA_VERSION_PROPERTIES);
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

    private static AdapterFactory getAdapterFactory() {
        final ServiceLoader<AdapterFactory> adapterFactoryLoader = ServiceLoader.load(AdapterFactory.class);
        final Optional<AdapterFactory> adapterFactory = adapterFactoryLoader.findFirst();
        return adapterFactory.orElseThrow(() -> new NoSuchElementException(
                ExaError.messageBuilder("E-VSCOMJAVA-29").message("No AdapterFactory was found.").toString()));
    }
}
