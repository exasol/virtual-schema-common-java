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
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public DropVirtualSchemaRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.DROP_VIRTUAL_SCHEMA);
    }

    @Override
    public String execute(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeDropVirtualSchemaRequest(this, metadata);
    }
}