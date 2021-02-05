package com.exasol.adapter.request;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

public class DummyRequest extends AbstractAdapterRequest {
    public DummyRequest(final SchemaMetadataInfo metadata) {
        super("DUMMY-ADAPTER", metadata, AdapterRequestType.CREATE_VIRTUAL_SCHEMA);
    }

    @Override
    public String execute(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata) {
        return null;
    }
}
