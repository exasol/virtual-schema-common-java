package com.exasol.adapter;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.metadata.*;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;

@ExtendWith(MockitoExtension.class)
class RequestDispatcherTest {
    private static final String DEFAULT_REQUEST_PARTS = "\"schemaMetadataInfo\" : { \"name\" : \"foo\" }, " //
            + "\"properties\" : { \"SQL_DIALECT\" : \"DUMMY\" }";
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
    void testDispatchCreateVtirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"createVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}";
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.createVirtualSchema(any(), any()))
                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).createVirtualSchema(any(), any(CreateVirtualSchemaRequest.class));
    }

    private SchemaMetadata createSchemaMetadata(final String rawRequest) {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(new ColumnMetadata("BAR", "", DataType.createDecimal(18, 0), true, false, "", ""));
        final List<TableMetadata> tables = new ArrayList<>();
        tables.add(new TableMetadata("FOO", null, columns, ""));
        final SchemaMetadata metadata = new SchemaMetadata(rawRequest, tables);
        return metadata;
    }

    @Test
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"dropVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}";
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).dropVirtualSchema(any(), any(DropVirtualSchemaRequest.class));
    }

    @Test
    void testDispatchRefreshRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"refresh\", " + DEFAULT_REQUEST_PARTS + "}";
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.refresh(any(), any()))
                .thenReturn(RefreshResponse.builder().schemaMetadata(metadata).build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).refresh(any(), any(RefreshRequest.class));
    }

    @Test
    void testDispatchSetPropertiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"setProperties\", " + DEFAULT_REQUEST_PARTS + "}";
        when(this.adapterMock.setProperties(any(), any())).thenReturn(SetPropertiesResponse.builder().build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).setProperties(any(), any(SetPropertiesRequest.class));
    }

    @Test
    void testDispatchGetCapabilitiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"getCapabilities\", " + DEFAULT_REQUEST_PARTS + "}";
        when(this.adapterMock.getCapabilities(any(), any())).thenReturn(GetCapabilitiesResponse.builder().build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).getCapabilities(any(), any(GetCapabilitiesRequest.class));
    }

    @Test
    void testDispatchPushdownRequest() throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"pushdown\",\n" //
                + "    " + DEFAULT_REQUEST_PARTS + ",\n" //
                + "    \"pushdownRequest\" :\n" //
                + "    {\n" //
                + "        \"type\" : \"select\",\n" //
                + "        \"from\" :\n" //
                + "        {\n" //
                + "             \"type\" : \"table\",\n" //
                + "             \"name\" : \"FOO\"\n" //
                + "        }\n" //
                + "    },\n" //
                + "    \"involvedTables\" :\n" //
                + "    [\n" //
                + "        {\n" //
                + "            \"name\" : \"FOO\",\n" //
                + "            \"columns\" :\n" //
                + "            [\n" //
                + "                {\n" //
                + "                    \"name\" : \"BAR\"," //
                + "                    \"dataType\" :\n" //
                + "                    {\n" //
                + "                        \"type\" : \"DECIMAL\",\n" //
                + "                        \"precision\" : 18,\n" //
                + "                        \"scale\" : 0\n" //
                + "                    }\n" //
                + "                }\n" //
                + "            ]\n" //
                + "        }\n" //
                + "    ]\n" //
                + "}";
        when(this.adapterMock.pushdown(any(), any()))
                .thenReturn(PushDownResponse.builder().pushDownSql("SELECT * FROM FOOBAR").build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).pushdown(any(), any(PushDownRequest.class));
    }

    @Test
    void testGetCapabilitiesResponse() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"getCapabilities\", " + DEFAULT_REQUEST_PARTS + "}";
        when(this.adapterMock.getCapabilities(any(), any())).thenReturn(GetCapabilitiesResponse.builder().build());
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(response, startsWith("{\"type\":\"getCapabilities\""));
    }
}