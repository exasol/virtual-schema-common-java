package com.exasol.adapter.request.parser;

import static com.exasol.adapter.metadata.DataType.ExaCharset.ASCII;
import static com.exasol.adapter.metadata.DataType.ExaCharset.UTF8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.metadata.*;

import jakarta.json.*;

class TablesMetadataParserTest {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    @Test
    void testParseMetadata() throws IOException {
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(ColumnMetadata.builder().name("ID").adapterNotes("").type(DataType.createDecimal(22, 0))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("USER_ID").adapterNotes("").type(DataType.createDecimal(18, 0))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("URL").adapterNotes("").type(DataType.createVarChar(1000, UTF8))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(
                ColumnMetadata.builder().name("REQUEST_TIME").adapterNotes("").type(DataType.createTimestamp(false, 3))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        expectedInvolvedTablesMetadata.add(new TableMetadata("CLICKS", "", tableColumns, ""));
        final JsonArray tablesAsJson = readInvolvedTablesFromJsonFile("target/test-classes/pushdown_request.json");
        final List<TableMetadata> tables = TablesMetadataParser.create().parse(tablesAsJson);
        assertThat(tables, equalTo(expectedInvolvedTablesMetadata));
    }

    private JsonArray readInvolvedTablesFromJsonFile(final String file) throws IOException {
        final String rawRequest = readFile(new File(file), Charset.defaultCharset());
        try (final JsonReader reader = Json.createReader(new StringReader(rawRequest))) {
            return reader.readObject().getJsonArray(RequestParserConstants.INVOLVED_TABLES_KEY);
        }
    }

    private String readFile(final File file, final Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }

    @Test
    void testParseTablesMetadataAllColumnsTypes() throws IOException {
        final List<TableMetadata> expectedInvolvedTablesMetadata = createExpectedTableMetadata();
        final JsonArray tablesAsJson = readInvolvedTablesFromJsonFile(
                "target/test-classes/pushdown_request_alltypes.json");
        final List<TableMetadata> tables = TablesMetadataParser.create().parse(tablesAsJson);
        assertThat(tables, equalTo(expectedInvolvedTablesMetadata));
    }

    private List<TableMetadata> createExpectedTableMetadata() {
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(ColumnMetadata.builder().name("C_DECIMAL").adapterNotes("").type(DataType.createDecimal(18, 2))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_DOUBLE").adapterNotes("").type(DataType.createDouble())
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_VARCHAR_UTF8_1").adapterNotes("")
                .type(DataType.createVarChar(10000, UTF8)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_VARCHAR_UTF8_2").adapterNotes("")
                .type(DataType.createVarChar(10000, UTF8)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_VARCHAR_ASCII").adapterNotes("")
                .type(DataType.createVarChar(10000, ASCII)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_CHAR_UTF8_1").adapterNotes("")
                .type(DataType.createChar(3, UTF8)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_CHAR_UTF8_2").adapterNotes("")
                .type(DataType.createChar(3, UTF8)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns
                .add(ColumnMetadata.builder().name("C_CHAR_ASCII").adapterNotes("").type(DataType.createChar(3, ASCII))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_DATE").adapterNotes("").type(DataType.createDate())
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(
                ColumnMetadata.builder().name("C_TIMESTAMP_1").adapterNotes("").type(DataType.createTimestamp(false, 3))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(
                ColumnMetadata.builder().name("C_TIMESTAMP_2").adapterNotes("").type(DataType.createTimestamp(false, 3))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(
                ColumnMetadata.builder().name("C_TIMESTAMP_3").adapterNotes("").type(DataType.createTimestamp(true, 3))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(
                ColumnMetadata.builder().name("C_TIMESTAMP_4").adapterNotes("").type(DataType.createTimestamp(false, 7))
                        .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_BOOLEAN").adapterNotes("").type(DataType.createBool())
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_GEOMETRY").adapterNotes("").type(DataType.createGeometry(1))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_HASHTYPE").adapterNotes("").type(DataType.createHashtype(16))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        tableColumns.add(ColumnMetadata.builder().name("C_INTERVAL_DS_1").adapterNotes("")
                .type(DataType.createIntervalDaySecond(2, 3)).nullable(true).identity(false).defaultValue("")
                .comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_INTERVAL_DS_2").adapterNotes("")
                .type(DataType.createIntervalDaySecond(3, 4)).nullable(true).identity(false).defaultValue("")
                .comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_INTERVAL_YM_1").adapterNotes("")
                .type(DataType.createIntervalYearMonth(2)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        tableColumns.add(ColumnMetadata.builder().name("C_INTERVAL_YM_2").adapterNotes("")
                .type(DataType.createIntervalYearMonth(3)).nullable(true).identity(false).defaultValue("").comment("")
                .build());
        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        expectedInvolvedTablesMetadata.add(new TableMetadata("T1", "", tableColumns, ""));
        return expectedInvolvedTablesMetadata;
    }

    private static JsonArrayBuilder arrayBuilder() {
        return JSON.createArrayBuilder();
    }

    private static JsonObjectBuilder objectBuilder() {
        return JSON.createObjectBuilder();
    }

    @Test
    void testParseColumnMetadataUsesBooleanDefaults() {
        final JsonArray tablesAsJson = arrayBuilder().add(objectBuilder()
                .add("name", "T1")
                .add("columns", arrayBuilder()
                        .add(objectBuilder().add("name", "DEFAULTS")
                                .add("dataType", objectBuilder().add("type", "DECIMAL").add("precision", 18)
                                        .add("scale", 0)))
                        .add(objectBuilder().add("name", "EXPLICIT_VALUES").add("isNullable", false)
                                .add("isIdentity", true)
                                .add("dataType", objectBuilder().add("type", "DECIMAL").add("precision", 18)
                                        .add("scale", 0)))))
                .build();

        final List<TableMetadata> tables = TablesMetadataParser.create().parse(tablesAsJson);

        final List<ColumnMetadata> expectedColumns = List.of(
                ColumnMetadata.builder().name("DEFAULTS").adapterNotes("")
                        .type(DataType.createDecimal(18, 0)).nullable(true).identity(false).defaultValue("").comment("")
                        .build(),
                ColumnMetadata.builder().name("EXPLICIT_VALUES").adapterNotes("")
                        .type(DataType.createDecimal(18, 0)).nullable(false).identity(true).defaultValue("").comment("")
                        .build());

        assertThat(tables, contains(new TableMetadata("T1", "", expectedColumns, "")));
    }

    @Test
    void testParseMetadataReadsAdapterNotes() {
        final JsonArray tablesAsJson = arrayBuilder().add(objectBuilder()
                .add("name", "T1")
                .add("adapterNotes", objectBuilder().add("table", true))
                .add("columns", arrayBuilder()
                        .add(objectBuilder().add("name", "C1")
                                .add("adapterNotes", "column notes")
                                .add("dataType", objectBuilder().add("type", "BOOLEAN")))
                        .add(objectBuilder().add("name", "C2")
                                .add("adapterNotes", objectBuilder().add("nested", 1))
                                .add("dataType", objectBuilder().add("type", "DATE")))
                        .add(objectBuilder().add("name", "C3")
                                .add("dataType", objectBuilder().add("type", "DOUBLE")))))
                .build();

        final List<TableMetadata> tables = TablesMetadataParser.create().parse(tablesAsJson);
        final TableMetadata table = tables.get(0);

        assertThat(table.getAdapterNotes(), equalTo("{\"table\":true}"));
        assertThat(table.getColumns(), contains(
                ColumnMetadata.builder().name("C1").adapterNotes("column notes").type(DataType.createBool())
                        .nullable(true).identity(false).defaultValue("").comment("").build(),
                ColumnMetadata.builder().name("C2").adapterNotes("{\"nested\":1}").type(DataType.createDate())
                        .nullable(true).identity(false).defaultValue("").comment("").build(),
                ColumnMetadata.builder().name("C3").adapterNotes("").type(DataType.createDouble()).nullable(true)
                        .identity(false).defaultValue("").comment("").build()));
    }

    @ParameterizedTest
    @MethodSource("invalidTableMetadata")
    void testParseMetadataThrowsException(final JsonArray tablesAsJson,
            final String expectedErrorMessage) {
        final TablesMetadataParser parser = TablesMetadataParser.create();
        final RequestParserException exception = assertThrows(RequestParserException.class, () -> parser.parse(tablesAsJson));
        assertThat(exception.getMessage(), equalTo(expectedErrorMessage));
    }

    private static Stream<Arguments> invalidTableMetadata() {
        return Stream.of(
                Arguments.of(arrayBuilder().add(objectBuilder().add("name", "T1")).build(),
                        "E-VSCOMJAVA-44: Failed to parse 'table 'T1'' because mandatory field 'columns' is missing."),
                Arguments.of(arrayBuilder().add(objectBuilder()
                        .add("name", "T1")
                        .add("columns", arrayBuilder()
                                .add(objectBuilder()
                                        .add("dataType", objectBuilder().add("type", "DECIMAL")
                                                .add("precision", 18).add("scale", 0)))))
                        .build(),
                        "E-VSCOMJAVA-44: Failed to parse 'column #0 of table 'T1'' because mandatory field 'name' is missing."),
                Arguments.of(arrayBuilder().add(objectBuilder()
                        .add("name", "T1")
                        .add("columns", arrayBuilder().add(objectBuilder().add("name", "C1"))))
                        .build(),
                        "E-VSCOMJAVA-44: Failed to parse 'column 'C1' of table 'T1'' because mandatory field 'dataType' is missing."),
                Arguments.of(arrayBuilder().add(objectBuilder()
                        .add("name", "T1")
                        .add("columns", arrayBuilder()
                                .add(objectBuilder().add("name", "C1")
                                        .add("dataType",
                                                objectBuilder().add("precision", 18).add("scale", 0)))))
                        .build(),
                        "E-VSCOMJAVA-44: Failed to parse 'column 'C1' of table 'T1' data type' because mandatory field 'type' is missing."),

                // Unsupported data types
                Arguments.of(createSingleColumnTable("VARCHAR",
                        objectBuilder().add("size", 10).add("characterSet", "INVALID").build()),
                        "E-VSCOMJAVA-19: Unsupported charset encountered: 'INVALID'."),
                Arguments.of(createSingleColumnTable("INTERVAL",
                        objectBuilder().add("fromTo", "CENTURY TO MINUTE").build()),
                        "E-VSCOMJAVA-20: Unsupported interval data type encountered: 'CENTURY TO MINUTE'."),
                Arguments.of(createSingleColumnTable("UNSUPPORTED_TYPE", objectBuilder().build()),
                        "E-VSCOMJAVA-18: Unsupported data type encountered: 'UNSUPPORTED_TYPE'."));
    }

    private static JsonArray createSingleColumnTable(final String type, final JsonObject typeAttributes) {
        final JsonObjectBuilder dataType = objectBuilder().add("type", type);
        for (final String key : typeAttributes.keySet()) {
            dataType.add(key, typeAttributes.get(key));
        }
        return arrayBuilder().add(objectBuilder()
                .add("name", "T1")
                .add("columns", arrayBuilder()
                        .add(objectBuilder().add("name", "C1").add("dataType", dataType))))
                .build();
    }
}
