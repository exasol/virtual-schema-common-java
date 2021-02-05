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
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public CreateVirtualSchemaRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }

    @Override
    public String execute(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeCreateVirtualSchemaRequest(this, metadata);
    }
}