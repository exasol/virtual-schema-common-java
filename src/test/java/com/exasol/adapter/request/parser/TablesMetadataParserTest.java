package com.exasol.adapter.request.parser;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.MetadataException;
import com.exasol.adapter.metadata.TableMetadata;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

class TablesMetadataParserTest {
    @Test
    void testParseTablesMetadata() throws IOException, MetadataException {
        final String file = "target/test-classes/pushdown_request_alltypes.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);

        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        final String tableName = "T1";
        final String tableAdapterNotes = "";
        final String tableComment = "";
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(new ColumnMetadata("C_DECIMAL", "", DataType.createDecimal(18, 2), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_DOUBLE", "", DataType.createDouble(), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_UTF8_1", "", DataType.createVarChar(10000, DataType.ExaCharset.UTF8),
                    true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_UTF8_2", "", DataType.createVarChar(10000, DataType.ExaCharset.UTF8),
                    true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_ASCII", "", DataType.createVarChar(10000, DataType.ExaCharset.ASCII),
                    true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_1", "", DataType.createChar(3, DataType.ExaCharset.UTF8), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_2", "", DataType.createChar(3, DataType.ExaCharset.UTF8), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_ASCII", "", DataType.createChar(3, DataType.ExaCharset.ASCII), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_DATE", "", DataType.createDate(), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_1", "", DataType.createTimestamp(false), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_2", "", DataType.createTimestamp(false), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_3", "", DataType.createTimestamp(true), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_BOOLEAN", "", DataType.createBool(), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_GEOMETRY", "", DataType.createGeometry(1), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_DS_1", "", DataType.createIntervalDaySecond(2, 3), true, false, "",
                    ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_DS_2", "", DataType.createIntervalDaySecond(3, 4), true, false, "",
                    ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_YM_1", "", DataType.createIntervalYearMonth(2), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_YM_2", "", DataType.createIntervalYearMonth(3), true, false, "", ""));
        expectedInvolvedTablesMetadata.add(new TableMetadata(tableName, tableAdapterNotes, tableColumns, tableComment));

        final JsonObject tablesAsJson;
        try (final JsonReader jr = Json.createReader(new StringReader(json))) {
            tablesAsJson = jr.readObject();
        }
        final TablesMetadataParser tablesMetadataParser = new TablesMetadataParser();
        final List<TableMetadata> tables = tablesMetadataParser.parse(tablesAsJson);
        assertObjectEquals(expectedInvolvedTablesMetadata, tables);
    }

    private <T> void assertObjectEquals(final T expected, final T actual) {
        assertTrue("Expected:\n" + expected + "\nactual:\n" + actual,
              new ReflectionEquals(actual, (String[]) null).matches(expected));
    }
}
