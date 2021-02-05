package com.exasol.adapter;

public class StubAdapterFactory implements VirtualSchemaAdapterFactory {
    @Override
    public VirtualSchemaAdapter createVirtualSchemaAdapter() {
        return new StubAdapter();
    }

    @Override
    public String getVirtualSchemaAdapterVersion() {
        return "Stub Adapter Version";
    }

    @Override
    public String getVirtualSchemaAdapterName() {
        return "Stub Adapter Name";
    }
}
