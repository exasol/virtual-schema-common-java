package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

public class AdapterRequest {
    
    public enum AdapterRequestType {
        CREATE_VIRTUAL_SCHEMA,
        DROP_VIRTUAL_SCHEMA,
        REFRESH,
        SET_PROPERTIES,
        GET_CAPABILITIES,
        PUSHDOWN
    }
    
    private final SchemaMetadataInfo schemaMetadataInfo;
    private final AdapterRequestType type;
    
    AdapterRequest(final SchemaMetadataInfo schemaMetadataInfo, final AdapterRequestType type) {
        this.schemaMetadataInfo = schemaMetadataInfo;
        this.type = type;
    }
    
    public SchemaMetadataInfo getSchemaMetadataInfo() {
        return schemaMetadataInfo;
    }
    
    public AdapterRequestType getType() {
        return type;
    }
}
