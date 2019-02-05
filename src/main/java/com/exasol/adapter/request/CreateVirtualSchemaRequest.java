package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

public class CreateVirtualSchemaRequest extends AdapterRequest {
    
    public CreateVirtualSchemaRequest(final SchemaMetadataInfo schemaMetadataInfo) {
        super(schemaMetadataInfo, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }
}
