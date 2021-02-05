package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to create a new Virtual Schema
 */
public class CreateVirtualSchemaRequest extends AbstractAdapterRequest {
    /**
     * Create a new request of type {@link CreateVirtualSchemaRequest}
     *
     * @param schemaMetadataInfo schema metadata
     */
    public CreateVirtualSchemaRequest(final SchemaMetadataInfo schemaMetadataInfo) {
        super(schemaMetadataInfo, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }
}