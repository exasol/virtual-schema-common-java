package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to create a new Virtual Schema
 */
public class CreateVirtualSchemaRequest extends AbstractAdapterRequest {
    /**
     * Create a new request of type {@link CreateVirtualSchemaRequest}
     *
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public CreateVirtualSchemaRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }
}