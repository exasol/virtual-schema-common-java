package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.request.AdapterRequest.AdapterRequestType;
import com.exasol.adapter.request.parser.RequestParser;

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
    public static RequestDispatcher getInstance() {
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
        final AdapterRequestType type = request.getType();
        final VirtualSchemaAdapter responsibleAdapter = AdapterRegistry.getInstance().getAdapterForName("DUMMY");
        switch (type) {
        case CREATE_VIRTUAL_SCHEMA:
            responsibleAdapter.createVirtualSchema(metadata, (CreateVirtualSchemaRequest) request);
            break;
        case DROP_VIRTUAL_SCHEMA:
            responsibleAdapter.dropVirtualSchema(metadata, (DropVirtualSchemaRequest) request);
            break;
        case REFRESH:
            responsibleAdapter.refresh(metadata, (RefreshRequest) request);
            break;
        case SET_PROPERTIES:
            responsibleAdapter.setProperties(metadata, (SetPropertiesRequest) request);
            break;
        case GET_CAPABILITIES:
            responsibleAdapter.getCapabilities(metadata, (GetCapabilitiesRequest) request);
            break;
        case PUSHDOWN:
            responsibleAdapter.pushdown(metadata, (PushdownRequest) request);
            break;
        default:
            throw new AdapterException("Unknown adapter request \"" + type + "\".");
        }
        return null;
    }
}