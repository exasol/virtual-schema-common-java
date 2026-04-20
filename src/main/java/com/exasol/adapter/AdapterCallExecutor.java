package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;
import com.exasol.adapter.response.converter.ResponseJsonConverter;
import com.exasol.telemetry.TelemetryClient;

/**
 * Executor for adapter calls issued by the Exasol database.
 */
public class AdapterCallExecutor {
    private final VirtualSchemaAdapter adapter;
    private final TelemetryClient telemetryClient;

    /**
     * Construct a new {@link AdapterCallExecutor}.
     *
     * @param adapter         an instance of {@link VirtualSchemaAdapter}
     * @param telemetryClient telemetry client for sending features usage to Exasol's telemetry service
     */
    public AdapterCallExecutor(final VirtualSchemaAdapter adapter, final TelemetryClient telemetryClient) {
        this.adapter = adapter;
        this.telemetryClient = telemetryClient;
    }

    /**
     * Execute an adapter call.
     * 
     * @param request  request coming from the core database.
     * @param metadata metadata
     * @return response in a JSON format
     * @throws AdapterException if something goes wrong
     */
    @SuppressWarnings("squid:S2139")
    protected String executeAdapterCall(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        return request.executeWith(this, metadata);
    }

    /**
     * Execute a create virtual schema request.
     * 
     * @param request  instance of {@link CreateVirtualSchemaRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeCreateVirtualSchemaRequest(final CreateVirtualSchemaRequest request,
            final ExaMetadata metadata) throws AdapterException {
        telemetryClient.track("createVirtualSchema");
        final CreateVirtualSchemaResponse response = this.adapter.createVirtualSchema(metadata, request);
        return ResponseJsonConverter.getInstance().convertCreateVirtualSchemaResponse(response);
    }

    /**
     * Execute a drop virtual schema request.
     *
     * @param request  instance of {@link DropVirtualSchemaRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeDropVirtualSchemaRequest(final DropVirtualSchemaRequest request, final ExaMetadata metadata)
            throws AdapterException {
        telemetryClient.track("dropVirtualSchema");
        final DropVirtualSchemaResponse response = this.adapter.dropVirtualSchema(metadata, request);
        return ResponseJsonConverter.getInstance().convertDropVirtualSchemaResponse(response);
    }

    /**
     * Execute a refresh virtual schema request.
     *
     * @param request  instance of {@link RefreshRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeRefreshRequest(final RefreshRequest request, final ExaMetadata metadata)
            throws AdapterException {
        telemetryClient.track("refreshVirtualSchema");
        final RefreshResponse response = this.adapter.refresh(metadata, request);
        return ResponseJsonConverter.getInstance().convertRefreshResponse(response);
    }

    /**
     * Execute a set properties virtual schema request.
     *
     * @param request  instance of {@link SetPropertiesRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeSetPropertiesRequest(final SetPropertiesRequest request, final ExaMetadata metadata)
            throws AdapterException {
        telemetryClient.track("setProperties");
        final SetPropertiesResponse response = this.adapter.setProperties(metadata, request);
        return ResponseJsonConverter.getInstance().convertSetPropertiesResponse(response);
    }

    /**
     * Execute a get capabilities virtual schema request.
     *
     * @param request  instance of {@link GetCapabilitiesRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeGetCapabilitiesRequest(final GetCapabilitiesRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final GetCapabilitiesResponse response = this.adapter.getCapabilities(metadata, request);
        return ResponseJsonConverter.getInstance().convertGetCapabilitiesResponse(response);
    }

    /**
     * Execute a push down virtual schema request.
     *
     * @param request  instance of {@link PushDownRequest}
     * @param metadata metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executePushDownRequest(final PushDownRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final PushDownResponse response = this.adapter.pushdown(metadata, request);
        return ResponseJsonConverter.getInstance().convertPushDownResponse(response);
    }
}
