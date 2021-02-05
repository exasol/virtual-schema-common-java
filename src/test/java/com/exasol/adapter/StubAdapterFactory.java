package com.exasol.adapter;

public class StubAdapterFactory implements AdapterFactory {
    @Override
    public VirtualSchemaAdapter createAdapter() {
        return new StubAdapter();
    }

    @Override
    public String getAdapterVersion() {
        return "Stub Adapter Version";
    }

    @Override
    public String getAdapterName() {
        return "Stub Adapter Name";
    }
}
