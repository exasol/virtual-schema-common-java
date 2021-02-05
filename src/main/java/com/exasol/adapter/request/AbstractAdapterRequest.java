package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * Abstract base class for all Virtual Schema Adapter requests
 */
public abstract class AbstractAdapterRequest implements AdapterRequest {
    private final SchemaMetadataInfo schemaMetadataInfo;
    private final AdapterRequestType type;

    AbstractAdapterRequest(final SchemaMetadataInfo schemaMetadataInfo, final AdapterRequestType type) {
        this.schemaMetadataInfo = schemaMetadataInfo;
        this.type = type;
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
    public String getVirtualSchemaName() {
        return this.schemaMetadataInfo.getSchemaName();
    }

    @Override
    public String toString() {
        return "AbstractAdapterRequest [schemaMetadataInfo=" + this.schemaMetadataInfo + ", type=" + this.type + "]";
    }
}