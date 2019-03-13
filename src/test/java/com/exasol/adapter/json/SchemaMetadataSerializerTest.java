package com.exasol.adapter.json;

import com.exasol.adapter.metadata.*;
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
    void serializeMetadataWithTables() throws MetadataException {
        final ColumnMetadata columnMetadata1 =
              new ColumnMetadata("columnName1", "columnNotes1", DataType.createDouble(), true, true, "default1",
                    "commentCol1");
        final ColumnMetadata columnMetadata2 =
              new ColumnMetadata("columnName2", "columnNotes2", DataType.createBool(), false, false, "default2",
                    "commentCol2");
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