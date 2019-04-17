package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.metadata.*;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;
import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemErrGuard;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SystemErrGuard.class)
class RequestDispatcherIT {
    private static final String DEFAULT_REQUEST_PARTS = "    \"schemaMetadataInfo\" :\n" //
            + "    {\n" //
            + "        \"name\" : \"foo\",\n" //
            + "        \"properties\" :\n" //
            + "        {\n" //
            + "            \"SQL_DIALECT\" : \"DUMMY\"\n" //
            + "        }\n" //
            + "    }\n";
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
    void testDispatchCreateVirtualSchemaRequest(final Capturable stream) throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + DEFAULT_REQUEST_PARTS //
                + "}";
        stream.capture();
        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
        when(this.adapterMock.createVirtualSchema(any(), any()))
                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(stream.getCapturedData(), containsString("version"));
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
}