package com.exasol.adapter;

public class StubAdapterFactory implements AdapterFactory {
    @Override
    public VirtualSchemaAdapter createAdapter(final AdapterContext context) {
        return new StubAdapter(context);
    }

    @Override
    public String getAdapterVersion() {
        return "Stub Adapter Version";
    }

    @Override
    public String getAdapterName() {
        return "Stub Adapter Name";
    }

    @Override
    public String getAdapterProjectShortTag() {
        return "VSSTUB";
    }
}
