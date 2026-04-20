package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class StubAdapterFactory implements AdapterFactory {
    @Override
    public VirtualSchemaAdapter createAdapter(final AdapterContext context) {
        assertThat(context.getTelemetryClient(), notNullValue());
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
