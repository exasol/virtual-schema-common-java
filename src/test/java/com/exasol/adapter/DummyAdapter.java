package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.CreateVirtualSchemaRequest;
import com.exasol.adapter.request.DropVirtualSchemaRequest;

public class DummyAdapter implements VirtualSchemaAdapter {
    private boolean dropVirtualSchemaCalled;

    public boolean wasDropVirtualSchemaHandlerCalled() {
        return this.dropVirtualSchemaCalled;
    }

    @Override
    public void dropVirtualSchema(final ExaMetadata metadata, final DropVirtualSchemaRequest request) {
        this.dropVirtualSchemaCalled = true;
    }

    @Override
    public void createVirtualSchema(final ExaMetadata metadata, final CreateVirtualSchemaRequest request) {
        // TODO Auto-generated method stub

    }
}