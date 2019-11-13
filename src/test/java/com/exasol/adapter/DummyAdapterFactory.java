package com.exasol.adapter;

import java.util.HashSet;
import java.util.Set;

public class DummyAdapterFactory implements AdapterFactory {
    @Override
    public Set<String> getSupportedAdapterNames() {
        final Set<String> names = new HashSet<>();
        names.add("Dummy");
        return names;
    }

    @Override
    public VirtualSchemaAdapter createAdapter() {
        return new DummyAdapter();
    }

    @Override
    public String getAdapterVersion() {
        return null;
    }

    @Override
    public String getAdapterName() {
        return null;
    }
}