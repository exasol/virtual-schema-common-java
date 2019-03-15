package com.exasol.adapter;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;

@ExtendWith(MockitoExtension.class)
class RequestDispatcherTest {
    private static final String SCHEMA_METADATA_INFO = "\"schemaMetadataInfo\" : { \"name\" : \"foo\" }";
    private final ExaMetadata metadata = null;
    @Mock
    private VirtualSchemaAdapter adapterMock;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        AdapterRegistry.getInstance().registerAdapter("DUMMY", this.adapterMock);
    }

    @AfterEach
    void AfterEach() {
        AdapterRegistry.getInstance().clear();
    }

    @Test
    void testDispatchCreateVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"createVirtualSchema\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).createVirtualSchema(ArgumentMatchers.any(),
                ArgumentMatchers.any(CreateVirtualSchemaRequest.class));
    }

    @Test
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"dropVirtualSchema\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).dropVirtualSchema(ArgumentMatchers.any(),
                ArgumentMatchers.any(DropVirtualSchemaRequest.class));
    }

    @Test
    void testRefreshRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"refresh\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).refresh(ArgumentMatchers.any(), ArgumentMatchers.any(RefreshRequest.class));
    }

    @Test
    void testSetPropertiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"setProperties\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).setProperties(ArgumentMatchers.any(),
                ArgumentMatchers.any(SetPropertiesRequest.class));
    }

    @Test
    void testGetCapabilitiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"getCapabilities\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).getCapabilities(ArgumentMatchers.any(),
                ArgumentMatchers.any(GetCapabilitiesRequest.class));
    }

    @Test
    void testPushdownRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"pushdown\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).pushdown(ArgumentMatchers.any(), ArgumentMatchers.any(PushdownRequest.class));
    }
}