package com.exasol.adapter;

import java.util.List;

import com.exasol.ExaMetadata;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;

/**
 * This class implements a stub for a VirtualSchemaAdapter
 */
public class StubAdapter implements VirtualSchemaAdapter {
    @Override
    public CreateVirtualSchemaResponse createVirtualSchema(final ExaMetadata metadata,
            final CreateVirtualSchemaRequest request) {
        return CreateVirtualSchemaResponse.builder().schemaMetadata(new SchemaMetadata("", List.of())).build();
    }

    @Override
    public DropVirtualSchemaResponse dropVirtualSchema(final ExaMetadata metadata,
            final DropVirtualSchemaRequest request) {
        return DropVirtualSchemaResponse.builder().build();
    }

    @Override
    public RefreshResponse refresh(final ExaMetadata metadata, final RefreshRequest request) {
        return RefreshResponse.builder().schemaMetadata(new SchemaMetadata("", List.of())).build();
    }

    @Override
    public SetPropertiesResponse setProperties(final ExaMetadata metadata, final SetPropertiesRequest request) {
        return SetPropertiesResponse.builder().schemaMetadata(new SchemaMetadata("", List.of())).build();
    }

    @Override
    public GetCapabilitiesResponse getCapabilities(final ExaMetadata metadata, final GetCapabilitiesRequest request) {
        return GetCapabilitiesResponse.builder().build();
    }

    @Override
    public PushDownResponse pushdown(final ExaMetadata metadata, final PushDownRequest request) {
        return PushDownResponse.builder().pushDownSql("SELECT * FROM FOOBAR").build();
    }

    @Override
    public String getName() {
        return "Stub Adapter Name";
    }

    @Override
    public String getVersion() {
        return "Stub Adapter Version";
    }
}