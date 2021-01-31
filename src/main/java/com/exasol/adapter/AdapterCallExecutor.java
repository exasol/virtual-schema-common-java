package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;
import com.exasol.adapter.response.converter.ResponseJsonConverter;
import com.exasol.errorreporting.ExaError;

/**
 * Executor for adapter calls issued by the Exasol database.
 */
public class AdapterCallExecutor {
    private final VirtualSchemaAdapter adapter;

    /**
     * Construct a new {@link AdapterCallExecutor}.
     *
     * @param adapter an instance of {@link VirtualSchemaAdapter}
     */
    public AdapterCallExecutor(final VirtualSchemaAdapter adapter) {
        this.adapter = adapter;
    }

    @SuppressWarnings("squid:S2139")
    protected String executeAdapterCall(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final AdapterRequestType type = request.getType();
        switch (type) {
        case CREATE_VIRTUAL_SCHEMA:
            return dispatchCreateVirtualSchemaRequestToAdapter(request, metadata);
        case DROP_VIRTUAL_SCHEMA:
            return dispatchDropVirtualSchemaRequestToAdapter(request, metadata);
        case REFRESH:
            return dispatchRefreshRequestToAdapter(request, metadata);
        case SET_PROPERTIES:
            return dispatchSetPropertiesRequestToAdapter(request, metadata);
        case GET_CAPABILITIES:
            return dispatchGetCapabilitiesRequestToAdapter(request, metadata);
        case PUSHDOWN:
            return dispatchPushDownRequestToAdapter(request, metadata);
        default:
            throw new AdapterException(ExaError.messageBuilder("E-VS-COM-JAVA-30")
                    .message("The request dispatcher encountered a request type {{type}} which it does not recognize.")
                    .ticketMitigation().parameter("type", type.toString()).toString());
        }
    }

    private String dispatchCreateVirtualSchemaRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final CreateVirtualSchemaResponse response = this.adapter.createVirtualSchema(metadata,
                (CreateVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertCreateVirtualSchemaResponse(response);
    }

    private String dispatchDropVirtualSchemaRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final DropVirtualSchemaResponse response = this.adapter.dropVirtualSchema(metadata,
                (DropVirtualSchemaRequest) request);
        return ResponseJsonConverter.getInstance().convertDropVirtualSchemaResponse(response);
    }

    private String dispatchRefreshRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final RefreshResponse response = this.adapter.refresh(metadata, (RefreshRequest) request);
        return ResponseJsonConverter.getInstance().convertRefreshResponse(response);
    }

    private String dispatchSetPropertiesRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final SetPropertiesResponse response = this.adapter.setProperties(metadata, (SetPropertiesRequest) request);
        return ResponseJsonConverter.getInstance().convertSetPropertiesResponse(response);
    }

    private String dispatchGetCapabilitiesRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final GetCapabilitiesResponse response = this.adapter.getCapabilities(metadata,
                (GetCapabilitiesRequest) request);
        return ResponseJsonConverter.getInstance().convertGetCapabilitiesResponse(response);
    }

    private String dispatchPushDownRequestToAdapter(final AdapterRequest request, final ExaMetadata metadata)
            throws AdapterException {
        final PushDownResponse response = this.adapter.pushdown(metadata, (PushDownRequest) request);
        return ResponseJsonConverter.getInstance().convertPushDownResponse(response);
    }
}
