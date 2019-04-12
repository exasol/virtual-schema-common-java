package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * Abstract base class for all Virtual Schema Adapter requests
 */
public abstract class AbstractAdapterRequest implements AdapterRequest {
    private final SchemaMetadataInfo schemaMetadataInfo;
    private final AdapterRequestType type;
    private final String adapterName;

    AbstractAdapterRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo,
            final AdapterRequestType type) {
        this.schemaMetadataInfo = schemaMetadataInfo;
        this.type = type;
        this.adapterName = adapterName;
    }

    @Override
    public SchemaMetadataInfo getSchemaMetadataInfo() {
        return this.schemaMetadataInfo;
    }

    @Override
    public AdapterRequestType getType() {
        return this.type;
    }

    @Override
    public String getAdapterName() {
        return this.adapterName;
    }

    @Override
    public String getVirtualSchemaName() {
        return this.schemaMetadataInfo.getSchemaName();
    }
}