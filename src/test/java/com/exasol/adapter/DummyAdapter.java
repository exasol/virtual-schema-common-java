package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;

/**
 * This class implements a stub for a VirtualSchemaAdapter
 */
public class DummyAdapter implements VirtualSchemaAdapter {
    @Override
    public CreateVirtualSchemaResponse createVirtualSchema(final ExaMetadata metadata,
            final CreateVirtualSchemaRequest request) {
        return null;
    }

    @Override
    public DropVirtualSchemaResponse dropVirtualSchema(final ExaMetadata metadata,
            final DropVirtualSchemaRequest request) {
        return null;
    }

    @Override
    public RefreshResponse refresh(final ExaMetadata metadata, final RefreshRequest request) {
        return null;
    }

    @Override
    public SetPropertiesResponse setProperties(final ExaMetadata metadata, final SetPropertiesRequest request) {
        return null;
    }

    @Override
    public GetCapabilitiesResponse getCapabilities(final ExaMetadata metadata, final GetCapabilitiesRequest request) {
        return null;
    }

    @Override
    public PushDownResponse pushdown(final ExaMetadata metadata, final PushDownRequest request) {
        return null;
    }
}