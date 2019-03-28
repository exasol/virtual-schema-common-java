package com.exasol.adapter.json;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.metadata.TableMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchemaMetadataSerializerTest {
    private static final String TEST_ADAPTER_NOTES_VALUE = "testValue";
    @Mock
    SchemaMetadata schemaMetadata;

    @BeforeEach
    void setUp() {
        when(this.schemaMetadata.getAdapterNotes()).thenReturn(TEST_ADAPTER_NOTES_VALUE);
    }

    @Test
    void serializeEmptyMetadata() {
        final JsonObjectBuilder jsonObjectBuilder = SchemaMetadataSerializer.serialize(this.schemaMetadata);
        assertThat(jsonObjectBuilder.build().toString(), equalTo("{\"tables\":[],\"adapterNotes\":\"testValue\"}"));
    }

    @Test
    void serializeMetadataWithTables() {
        final ColumnMetadata columnMetadata1 =
              ColumnMetadata.builder().name("columnName1").adapterNotes("columnNotes1").type(DataType.createDouble())
                    .nullable(true).identity(true).defaultValue("default1").comment("commentCol1").build();
        final ColumnMetadata columnMetadata2 =
              ColumnMetadata.builder().name("columnName2").adapterNotes("columnNotes2").type(DataType.createBool())
                    .nullable(false).identity(false).defaultValue("default2").comment("commentCol2").build();

        final List<ColumnMetadata> firstTableColumns = new ArrayList<>();
        firstTableColumns.add(columnMetadata1);
        final List<ColumnMetadata> secondTableColumns = new ArrayList<>();
        secondTableColumns.add(columnMetadata2);

        final TableMetadata tableMetadata1 =
              new TableMetadata("tableName1", "tableNotes1", firstTableColumns, "comment1");
        final TableMetadata tableMetadata2 =
              new TableMetadata("tableName2", "tableNotes2", secondTableColumns, "comment2");

        final List<TableMetadata> tables = new ArrayList<>();
        tables.add(tableMetadata1);
        tables.add(tableMetadata2);
        when(this.schemaMetadata.getTables()).thenReturn(tables);
        final JsonObjectBuilder jsonObjectBuilder = SchemaMetadataSerializer.serialize(this.schemaMetadata);
        assertThat(jsonObjectBuilder.build().toString(), //
              equalTo("{\"tables\"" //
                    + ":[{\"type\":\"table\"," //
                    + "\"name\":\"tableName1\"," //
                    + "\"adapterNotes\":\"tableNotes1\"," //
                    + "\"comment\":\"comment1\"," //
                    + "\"columns\":[{\"name\":\"columnName1\",\"adapterNotes\":\"columnNotes1\"," //
                    + "\"dataType\":{\"type\":\"double\"}," //
                    + "\"isIdentity\":true,\"default\":\"default1\",\"comment\":\"commentCol1\"}]}," //
                    + "{\"type\":\"table\"," //
                    + "\"name\":\"tableName2\","//
                    + "\"adapterNotes\":\"tableNotes2\"," //
                    + "\"comment\":\"comment2\"," //
                    + "\"columns\":[{\"name\":\"columnName2\",\"adapterNotes\":\"columnNotes2\"," //
                    + "\"dataType\":{\"type\":\"boolean\"}," //
                    + "\"isNullable\":false,\"default\":\"default2\",\"comment\":\"commentCol2\"}]}]," //
                    + "\"adapterNotes\":\"testValue\"}"));
    }
}