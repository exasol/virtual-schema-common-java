package com.exasol.adapter.request;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to present a list of its capabilities
 */
public class GetCapabilitiesRequest extends AbstractAdapterRequest {
    /**
     * Create a new request of type {@link GetCapabilitiesRequest}
     *
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public GetCapabilitiesRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.GET_CAPABILITIES);
    }

    @Override
    public String execute(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeGetCapabilitiesRequest(this, metadata);
    }
}