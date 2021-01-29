package com.exasol.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.itsallcode.junit.sysextensions.SystemErrGuard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SystemErrGuard.class)
class AdapterCallExecutorTest {
    @Mock
    private VirtualSchemaAdapter mockAdapter;
    private AdapterCallExecutor adapterCallExecutor;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.adapterCallExecutor = new AdapterCallExecutor(this.mockAdapter);
    }

    @Test
    void testDispatchCreateVtirtualSchemaRequest() throws AdapterException {
        final CreateVirtualSchemaResponse expectedResponse = CreateVirtualSchemaResponse.builder()
                .schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.createVirtualSchema(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null,
                new CreateVirtualSchemaRequest(null, null));
        assertEquals(response,
                "{\"type\":\"createVirtualSchema\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
        verify(this.mockAdapter).createVirtualSchema(any(), any(CreateVirtualSchemaRequest.class));
    }

    private SchemaMetadata getSchemaMetadata() {
        return new SchemaMetadata("", List.of());
    }

    @Test
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        final DropVirtualSchemaResponse expectedResponse = DropVirtualSchemaResponse.builder().build();
        when(this.mockAdapter.dropVirtualSchema(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null,
                new DropVirtualSchemaRequest(null, null));
        assertEquals(response, "{\"type\":\"dropVirtualSchema\"}");
        verify(this.mockAdapter).dropVirtualSchema(any(), any(DropVirtualSchemaRequest.class));
    }

    @Test
    void testDispatchRefreshRequest() throws AdapterException {
        final RefreshResponse expectedResponse = RefreshResponse.builder().schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.refresh(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null, new RefreshRequest(null, null));
        assertEquals(response, "{\"type\":\"refresh\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
        verify(this.mockAdapter).refresh(any(), any(RefreshRequest.class));
    }

    @Test
    void testDispatchSetPropertiesRequest() throws AdapterException {
        final SetPropertiesResponse expectedResponse = SetPropertiesResponse.builder()
                .schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.setProperties(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null,
                new SetPropertiesRequest(null, null, null));
        assertEquals(response, "{\"type\":\"setProperties\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
        verify(this.mockAdapter).setProperties(any(), any(SetPropertiesRequest.class));
    }

    @Test
    void testDispatchGetCapabilitiesRequest() throws AdapterException {
        final GetCapabilitiesResponse expectedResponse = GetCapabilitiesResponse.builder().build();
        when(this.mockAdapter.getCapabilities(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null,
                new GetCapabilitiesRequest(null, null));
        assertEquals(response, "{\"type\":\"getCapabilities\",\"capabilities\":[]}");
        verify(this.mockAdapter).getCapabilities(any(), any(GetCapabilitiesRequest.class));
    }

    @Test
    void testDispatchPushDownRequest() throws AdapterException {
        final PushDownResponse expectedResponse = PushDownResponse.builder().pushDownSql("SELECT * FROM FOOBAR")
                .build();
        when(this.mockAdapter.pushdown(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(null,
                new PushDownRequest(null, null, null, null));
        assertEquals(response, "{\"type\":\"pushdown\",\"sql\":\"SELECT * FROM FOOBAR\"}");
        verify(this.mockAdapter).pushdown(any(), any(PushDownRequest.class));
    }
}