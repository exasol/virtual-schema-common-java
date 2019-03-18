package com.exasol.adapter.request.parser;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.MetadataException;
import com.exasol.adapter.metadata.TableMetadata;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.exasol.adapter.metadata.DataType.ExaCharset.ASCII;
import static com.exasol.adapter.metadata.DataType.ExaCharset.UTF8;
import static com.exasol.adapter.metadata.DataType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class TablesMetadataParserTest {
    @Test
    void testParseMetadata() throws IOException, AdapterException {
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(new ColumnMetadata("ID", "", DataType.createDecimal(22, 0), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("USER_ID", "", DataType.createDecimal(18, 0), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("URL", "", createVarChar(1000, UTF8), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("REQUEST_TIME", "", DataType.createTimestamp(false), true, true, "", ""));
        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        expectedInvolvedTablesMetadata.add(new TableMetadata("CLICKS", "", tableColumns, ""));

        final String file = "target/test-classes/pushdown_request.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);
        final JsonObject tablesAsJson;
        try (final JsonReader jr = Json.createReader(new StringReader(json))) {
            tablesAsJson = jr.readObject();
        }

        final TablesMetadataParser tablesMetadataParser = new TablesMetadataParser();
        final List<TableMetadata> tables = tablesMetadataParser.parse(tablesAsJson);
        assertThat(tables, equalTo(expectedInvolvedTablesMetadata));
    }

    @Test
    void testParseTablesMetadataAllColumnsTypes() throws IOException, MetadataException {
        final List<TableMetadata> expectedInvolvedTablesMetadata = getExpectedTableMetadata();

        final String file = "target/test-classes/pushdown_request_alltypes.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);
        final JsonObject tablesAsJson;
        try (final JsonReader jr = Json.createReader(new StringReader(json))) {
            tablesAsJson = jr.readObject();
        }

        final TablesMetadataParser tablesMetadataParser = new TablesMetadataParser();
        final List<TableMetadata> tables = tablesMetadataParser.parse(tablesAsJson);
        assertThat(tables, equalTo(expectedInvolvedTablesMetadata));
    }

    private List<TableMetadata> getExpectedTableMetadata() throws MetadataException {
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(new ColumnMetadata("C_DECIMAL", "", DataType.createDecimal(18, 2), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_DOUBLE", "", DataType.createDouble(), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_VARCHAR_UTF8_1", "", createVarChar(10000, UTF8), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_VARCHAR_UTF8_2", "", createVarChar(10000, UTF8), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_VARCHAR_ASCII", "", createVarChar(10000, ASCII), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_1", "", DataType.createChar(3, UTF8), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_2", "", DataType.createChar(3, UTF8), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_ASCII", "", DataType.createChar(3, ASCII), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_DATE", "", DataType.createDate(), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_1", "", DataType.createTimestamp(false), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_2", "", DataType.createTimestamp(false), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_3", "", DataType.createTimestamp(true), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_BOOLEAN", "", DataType.createBool(), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_GEOMETRY", "", DataType.createGeometry(1), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_INTERVAL_DS_1", "", createIntervalDaySecond(2, 3), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_INTERVAL_DS_2", "", createIntervalDaySecond(3, 4), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_INTERVAL_YM_1", "", createIntervalYearMonth(2), true, true, "", ""));
        tableColumns.add(new ColumnMetadata("C_INTERVAL_YM_2", "", createIntervalYearMonth(3), true, true, "", ""));

        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        expectedInvolvedTablesMetadata.add(new TableMetadata("T1", "", tableColumns, ""));
        return expectedInvolvedTablesMetadata;
    }
}
