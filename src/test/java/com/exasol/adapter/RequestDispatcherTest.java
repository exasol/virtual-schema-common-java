package com.exasol.adapter;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.CreateVirtualSchemaRequest;
import com.exasol.adapter.request.DropVirtualSchemaRequest;

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
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"dropVirtualSchema\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).dropVirtualSchema(ArgumentMatchers.any(),
                ArgumentMatchers.any(DropVirtualSchemaRequest.class));
    }

    @Test
    void testDispatchCreateVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"createVirtualSchema\", " + SCHEMA_METADATA_INFO + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).createVirtualSchema(ArgumentMatchers.any(),
                ArgumentMatchers.any(CreateVirtualSchemaRequest.class));
    }
}