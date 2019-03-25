package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.request.parser.RequestParser;
import com.exasol.adapter.response.*;
import com.exasol.adapter.response.converter.ResponseJsonConverter;
import com.exasol.logging.RemoteLogManager;

/**
 * This class is the main entry point for calls to a Virtual Schema. From here the adapter calls are dispatched to the
 * responsible adapter.
 */
public final class RequestDispatcher {
    private static RequestDispatcher instance = new RequestDispatcher();

    /**
     * Get the singleton instance of the {@link RequestDispatcher}
     *
     * @return singleton instance
     */
    public static synchronized RequestDispatcher getInstance() {
        return instance;
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
        return getInstance().excecuteAdapterCall(metadata, rawRequest);
    }

    private String excecuteAdapterCall(final ExaMetadata metadata, final String rawRequest) throws AdapterException {
        final AdapterRequest request = new RequestParser().parse(rawRequest);
        configureAdapterLoggingAccordingToRequestSettings(request);
        final AdapterRequestType type = request.getType();
        final VirtualSchemaAdapter adapter = findResponsibleAdapter(request);
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

    private VirtualSchemaAdapter findResponsibleAdapter(final AdapterRequest request) {
        final String name = request.getAdapterName();
        return AdapterRegistry.getInstance().getAdapterForName(name);
    }

    private String dispatchCreateVirtualSchemaRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) {
        final CreateVirtualSchemaResponse response = adapter.createVirtualSchema(metadata,
                (CreateVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertCreateVirtualSchemaResponse(response);
    }

    private String dispatchDropVirtualSchemaRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) {
        final DropVirtualSchemaResponse response = adapter.dropVirtualSchema(metadata,
                (DropVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertDropVirtualSchemaResponse(response);
    }

    private String dispatchRefreshRequestToAdapter(final AdapterRequest request, final VirtualSchemaAdapter adapter,
            final ExaMetadata metadata) {
        final RefreshResponse response = adapter.refresh(metadata, (RefreshRequest) request);
        return ResponseJsonConverter.getInstance().convertRefreshResponse(response);
    }

    private String dispatchSetPropertiesRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) {
        final SetPropertiesResponse response = adapter.setProperties(metadata, (SetPropertiesRequest) request);
        return ResponseJsonConverter.getInstance().convertSetPropertiesResponse(response);
    }

    private String dispatchGetCapabilitiesRequestToAdapter(final AdapterRequest request,
            final VirtualSchemaAdapter adapter, final ExaMetadata metadata) {
        final GetCapabilitiesResponse response = adapter.getCapabilities(metadata, (GetCapabilitiesRequest) request);
        return ResponseJsonConverter.getInstance().convertGetCapabilitiesResponse(response);
    }

    private String dispatchPushDownRequestToAdapter(final AdapterRequest request, final VirtualSchemaAdapter adapter,
            final ExaMetadata metadata) {
        final PushDownResponse response = adapter.pushdown(metadata, (PushDownRequest) request);
        return ResponseJsonConverter.getInstance().convertPushDownResponse(response);
    }
}