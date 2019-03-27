package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

public class DummyRequest extends AbstractAdapterRequest {

    public DummyRequest(final SchemaMetadataInfo metadata) {
        super("DUMMY-ADAPTER", metadata, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }
}
