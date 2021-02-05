package com.exasol.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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
    void testExecuteCreateVtirtualSchemaRequest() throws AdapterException {
        final CreateVirtualSchemaResponse expectedResponse = CreateVirtualSchemaResponse.builder()
                .schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.createVirtualSchema(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new CreateVirtualSchemaRequest(null), null);
        assertEquals("{\"type\":\"createVirtualSchema\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}",
                response);
        verify(this.mockAdapter).createVirtualSchema(any(), any(CreateVirtualSchemaRequest.class));
    }

    private SchemaMetadata getSchemaMetadata() {
        return new SchemaMetadata("", List.of());
    }

    @Test
    void testExecuteDropVirtualSchemaRequest() throws AdapterException {
        final DropVirtualSchemaResponse expectedResponse = DropVirtualSchemaResponse.builder().build();
        when(this.mockAdapter.dropVirtualSchema(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new DropVirtualSchemaRequest(null), null);
        assertEquals("{\"type\":\"dropVirtualSchema\"}", response);
        verify(this.mockAdapter).dropVirtualSchema(any(), any(DropVirtualSchemaRequest.class));
    }

    @Test
    void testExecuteRefreshRequest() throws AdapterException {
        final RefreshResponse expectedResponse = RefreshResponse.builder().schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.refresh(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new RefreshRequest(null), null);
        assertEquals("{\"type\":\"refresh\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}", response);
        verify(this.mockAdapter).refresh(any(), any(RefreshRequest.class));
    }

    @Test
    void testExecuteSetPropertiesRequest() throws AdapterException {
        final SetPropertiesResponse expectedResponse = SetPropertiesResponse.builder()
                .schemaMetadata(getSchemaMetadata()).build();
        when(this.mockAdapter.setProperties(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new SetPropertiesRequest(null, null), null);
        assertEquals("{\"type\":\"setProperties\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}", response);
        verify(this.mockAdapter).setProperties(any(), any(SetPropertiesRequest.class));
    }

    @Test
    void testExecuteGetCapabilitiesRequest() throws AdapterException {
        final GetCapabilitiesResponse expectedResponse = GetCapabilitiesResponse.builder().build();
        when(this.mockAdapter.getCapabilities(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new GetCapabilitiesRequest(null), null);
        assertEquals("{\"type\":\"getCapabilities\",\"capabilities\":[]}", response);
        verify(this.mockAdapter).getCapabilities(any(), any(GetCapabilitiesRequest.class));
    }

    @Test
    void testExecutePushDownRequest() throws AdapterException {
        final PushDownResponse expectedResponse = PushDownResponse.builder().pushDownSql("SELECT * FROM FOOBAR")
                .build();
        when(this.mockAdapter.pushdown(any(), any())).thenReturn(expectedResponse);
        final String response = this.adapterCallExecutor.executeAdapterCall(new PushDownRequest(null, null, null),
                null);
        assertEquals("{\"type\":\"pushdown\",\"sql\":\"SELECT * FROM FOOBAR\"}", response);
        verify(this.mockAdapter).pushdown(any(), any(PushDownRequest.class));
    }
}