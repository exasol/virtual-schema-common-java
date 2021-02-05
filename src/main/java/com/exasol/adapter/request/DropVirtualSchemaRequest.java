package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to drop a Virtual Schema
 */
public class DropVirtualSchemaRequest extends AbstractAdapterRequest {
    /**
     * Create a new request of type {@link DropVirtualSchemaRequest}
     *
     * @param schemaMetadataInfo schema metadata
     */
    public DropVirtualSchemaRequest(final SchemaMetadataInfo schemaMetadataInfo) {
        super(schemaMetadataInfo, AdapterRequestType.DROP_VIRTUAL_SCHEMA);
    }
}