package com.exasol.adapter;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.request.parser.RequestParser;
import com.exasol.adapter.response.*;
import com.exasol.adapter.response.converter.ResponseJsonConverter;
import com.exasol.logging.RemoteLogManager;
import com.exasol.logging.VersionCollector;

/**
 * This class is the main entry point for calls to a Virtual Schema. From here the adapter calls are dispatched to the
 * responsible adapter.
 */
public final class RequestDispatcher {
    private static final RequestDispatcher INSTANCE = new RequestDispatcher();
    private static final Logger LOGGER = Logger.getLogger(RequestDispatcher.class.getName());

    /**
     * Get the singleton instance of the {@link RequestDispatcher}
     *
     * @return singleton instance
     */
    public static synchronized RequestDispatcher getInstance() {
        return INSTANCE;
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
        return getInstance().executeAdapterCall(metadata, rawRequest);
    }

    @SuppressWarnings("squid:S2139")
    private String executeAdapterCall(final ExaMetadata metadata, final String rawRequest) throws AdapterException {
        try {
            logVersionInformation();
            logRawRequest(rawRequest);
            final AdapterRequest request = new RequestParser().parse(rawRequest);
            configureAdapterLoggingAccordingToRequestSettings(request);
            final VirtualSchemaAdapter adapter = findResponsibleAdapter(request);
            return processRequest(request, adapter, metadata);
        } catch (final Exception exception) {
            LOGGER.severe(exception::getMessage);
            LOGGER.log(Level.FINE, "Stack trace:", exception);
            throw exception;
        }
    }

    private VirtualSchemaAdapter findResponsibleAdapter(final AdapterRequest request) {
        final String name = request.getAdapterName();
        return AdapterRegistry.getInstance().getAdapterForName(name);
    }

    private String processRequest(final AdapterRequest request, final VirtualSchemaAdapter adapter,
            final ExaMetadata metadata) throws AdapterException {
        final AdapterRequestType type = request.getType();
        switch (type) {
        case CREATE_VIRTUAL_SCHEMA:
            return dispatchCreateVirtualSchemaRequestToAdapter(request, adapter, metadata);
        case DROP_VIRTUAL_SCHEMA:
            return dispatchDropVirtualSchemaRequestToAdapter(request, adapter, metadata);
        case REFRESH:
            return dispatchRefreshRequestToAdapter(request, adapter, metadata);
        case SET_PROPERTIES:
            return dispatchSetPropertiesRequestToAdapter(request, adapter, metadata);
        case GET_CAPABILITIES:
            return dispatchGetCapabilitiesRequestToAdapter(request, adapter, metadata);
        case PUSHDOWN:
            return dispatchPushDownRequestToAdapter(request, adapter, metadata);
        default:
            throw new AdapterException("The request dispatcher encountered a request type \"" + type.toString()
                    + "\" which it does not recognize. Please create an issue ticket quoting this error message.");
        }
    }

    private void configureAdapterLoggingAccordingToRequestSettings(final AdapterRequest request) {
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

    private void logVersionInformation() {
        final VersionCollector versionCollector = new VersionCollector();
        LOGGER.info("Loaded versions: virtual-schema-common-java " + versionCollector.getVersionNumber());
    }

    private void logRawRequest(final String rawRequest) {
        LOGGER.finer(() -> "Raw JSON request:\n" + rawRequest);
    }

    private String dispatchCreateVirtualSchemaRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) throws AdapterException {
        final CreateVirtualSchemaResponse response = adapter.createVirtualSchema(metadata,
                (CreateVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertCreateVirtualSchemaResponse(response);
    }

    private String dispatchDropVirtualSchemaRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) throws AdapterException {
        final DropVirtualSchemaResponse response = adapter.dropVirtualSchema(metadata,
                (DropVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertDropVirtualSchemaResponse(response);
    }

    private String dispatchRefreshRequestToAdapter(final AdapterRequest request, final VirtualSchemaAdapter adapter,
            final ExaMetadata metadata) throws AdapterException {
        final RefreshResponse response = adapter.refresh(metadata, (RefreshRequest) request);
        return ResponseJsonConverter.getInstance().convertRefreshResponse(response);
    }

    private String dispatchSetPropertiesRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) throws AdapterException {
        final SetPropertiesResponse response = adapter.setProperties(metadata, (SetPropertiesRequest) request);
        return ResponseJsonConverter.getInstance().convertSetPropertiesResponse(response);
    }

    private String dispatchGetCapabilitiesRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) throws AdapterException {
        final GetCapabilitiesResponse response = adapter.getCapabilities(metadata, (GetCapabilitiesRequest) request);
        return ResponseJsonConverter.getInstance().convertGetCapabilitiesResponse(response);
    }

    private String dispatchPushDownRequestToAdapter(final AdapterRequest request, final VirtualSchemaAdapter adapter,
            final ExaMetadata metadata) throws AdapterException {
        final PushDownResponse response = adapter.pushdown(metadata, (PushDownRequest) request);
        return ResponseJsonConverter.getInstance().convertPushDownResponse(response);
    }
}