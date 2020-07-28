package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemErrGuard;
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
@ExtendWith(SystemErrGuard.class)
class RequestDispatcherTest {
    private static final String MOCKADAPTER = "MOCKADAPTER";

    private static final String DEFAULT_REQUEST_PARTS = "    \"schemaMetadataInfo\" :\n" //
            + "    {\n" //
            + "        \"name\" : \"foo\",\n" //
            + "        \"properties\" :\n" //
            + "        {\n" //
            + "            \"SQL_DIALECT\" : \"MOCKADAPTER\"\n" //
            + "        }\n" //
            + "    }\n";
    private final ExaMetadata metadata = null;
    @Mock
    private VirtualSchemaAdapter adapterMock;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        AdapterRegistry.getInstance().registerAdapterFactory(MOCKADAPTER,
                new MockInjectingAdapterFactory(this.adapterMock));
    }

    @AfterEach
    void AfterEach() {
        AdapterRegistry.getInstance().clear();
    }

    @Test
    void testDispatchCreateVtirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + DEFAULT_REQUEST_PARTS //
                + "}";
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.createVirtualSchema(any(), any()))
                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        verify(this.adapterMock).createVirtualSchema(any(), any(CreateVirtualSchemaRequest.class));
    }

    private SchemaMetadata createSchemaMetadata(final String rawRequest) {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(ColumnMetadata.builder().name("BAR").adapterNotes("").type(DataType.createDecimal(18, 0))
                .nullable(true).identity(false).defaultValue("").comment("").build());
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
    void testDispatchPushDownRequest() throws AdapterException {
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
        final String rawRequest = "{\n" //
                + "    \"type\" : \"getCapabilities\",\n" //
                + DEFAULT_REQUEST_PARTS //
                + "}";
        when(this.adapterMock.getCapabilities(any(), any())).thenReturn(GetCapabilitiesResponse.builder().build());
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(response, startsWith("{\"type\":\"getCapabilities\""));
    }

    @Test
    void testLoggingSetAccordingProperties(final Capturable stream) throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "          \"name\" : \"REMOTE_DEBUG_TEST\",\n" //
                + "          \"properties\" :\n" //
                + "          {\n" //
                + "              \"SQL_DIALECT\" : \"MOCKADAPTER\",\n" //
                + "              \"LOG_LEVEL\" : \"FINE\"\n" //
                + "          }\n" //
                + "    }\n" //
                + "}";
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.createVirtualSchema(any(), any()))
                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        stream.capture();
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(stream.getCapturedData(), containsString("level FINE."));
    }

    @Test
    void testRemoteLoggingSetAccordingPropertiesWithUnknownHostAndFallback(final Capturable stream)
            throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "          \"name\" : \"REMOTE_DEBUG_TEST\",\n" //
                + "          \"properties\" :\n" //
                + "          {\n" //
                + "              \"SQL_DIALECT\" : \"MOCKADAPTER\",\n" //
                + "              \"DEBUG_ADDRESS\" : \"this.host.does.not.exist.exasol.com\"\n" //
                + "          }\n" //
                + "    }\n" //
                + "}";
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.createVirtualSchema(any(), any()))
                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        stream.capture();
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(stream.getCapturedData(), containsString("Falling back to console log."));
    }

    @Test
    void testExecuteAdapterCallThrowsException(final Capturable stream) throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "          \"name\" : \"EXCEPTION_TEST\",\n" //
                + "          \"properties\" :\n" //
                + "          {\n" //
                + "              \"SQL_DIALECT\" : \"NON-EXISTENT\"\n" //
                + "          }\n" //
                + "    }\n" //
                + "}";
        stream.capture();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> RequestDispatcher.adapterCall(this.metadata, rawRequest)),
                () -> assertThat(stream.getCapturedData(), containsString("SEVERE")));
    }

    private static class MockInjectingAdapterFactory implements AdapterFactory {
        private final VirtualSchemaAdapter adapterMock;

        public MockInjectingAdapterFactory(final VirtualSchemaAdapter adapterMock) {
            this.adapterMock = adapterMock;
        }

        @Override
        public Set<String> getSupportedAdapterNames() {
            final Set<String> names = new HashSet<>();
            names.add(MOCKADAPTER);
            return names;
        }

        @Override
        public VirtualSchemaAdapter createAdapter() {
            return this.adapterMock;
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
}