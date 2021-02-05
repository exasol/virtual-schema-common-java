package com.exasol.adapter.request;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
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

    @Override
    public String executeWith(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeDropVirtualSchemaRequest(this, metadata);
    }
}