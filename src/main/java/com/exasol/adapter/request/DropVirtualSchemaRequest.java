package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to drop a Virtual Schema
 */
public class DropVirtualSchemaRequest extends AbstractAdapterRequest {
    /**
     * Create a new request of type {@link DropVirtualSchemaRequest}
     *
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public DropVirtualSchemaRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.DROP_VIRTUAL_SCHEMA);
    }
}