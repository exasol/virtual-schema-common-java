package com.exasol.adapter.request;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
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

    @Override
    public String executeWith(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeCreateVirtualSchemaRequest(this, metadata);
    }
}