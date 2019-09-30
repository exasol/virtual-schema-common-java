package com.exasol.adapter.metadata.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.DataType.ExaCharset;

class SchemaMetadataJsonConverterTest {
    private static final SchemaMetadataJsonConverter CONVERTER = SchemaMetadataJsonConverter.getInstance();
    private static final String SCHEMA_NAME = "TEST_SCHEMA";

    @Test
    void testConvert() throws JSONException {
        final String expected = "{\n" //
                + "  \"tables\" : [ {\n" //
                + "      \"type\" : \"table\",\n" //
                + "      \"name\" : \"table_A\",\n" //
                + "      \"adapterNotes\" : \"notes A\",\n" //
                + "      \"comment\" : \"comment A\",\n" //
                + "      \"columns\" : [ {\n" //
                + "          \"name\" : \"column_A1\",\n" //
                + "          \"adapterNotes\" : \"notes A1\",\n" //
                + "          \"dataType\" : { \"type\" : \"double\" },\n" //
                + "          \"isNullable\" : false,\n" //
                + "          \"comment\" = \"comment A1\"\n" //
                + "         }, {\n" //
                + "          \"name\" : \"column_A2\",\n" //
                + "          \"dataType\" : { \"type\" : \"date\" },\n" //
                + "          \"default\" : \"default A2\",\n" //
                + "          \"isIdentity\" : true\n" //
                + "         }\n" //
                + "       ]\n" //
                + "    }, {\n" //
                + "      \"type\" : \"table\",\n" //
                + "      \"columns\" : [ {\n" //
                + "          \"name\" : \"COLUMN_B1\",\n" //
                + "          \"dataType\" : { \"type\" : \"boolean\" }\n" //
                + "        }\n" //
                + "      ]\n" //
                + "    }\n" //
                + "  ]\n" //
                + "}";
        final List<TableMetadata> tables = new ArrayList<>();
        final List<ColumnMetadata> columnsA = new ArrayList<>();
        columnsA.add(new ColumnMetadata.Builder().name("column_A1").comment("comment A1").type(DataType.createDouble())
                .adapterNotes("notes A1").nullable(false).build());
        columnsA.add(new ColumnMetadata.Builder().name("column_A2").type(DataType.createDate())
                .defaultValue("default A2").identity(true).build());
        final List<ColumnMetadata> columnsB = new ArrayList<>();
        columnsB.add(new ColumnMetadata.Builder().name("COLUMN_B1").type(DataType.createBool()).build());
        tables.add(new TableMetadata("table_A", "notes A", columnsA, "comment A"));
        tables.add(new TableMetadata("TABLE_B", null, columnsB, null));
        final SchemaMetadata schemaMetadata = new SchemaMetadata(SCHEMA_NAME, tables);
        final JsonObject actual = CONVERTER.convert(schemaMetadata);
        JSONAssert.assertEquals(expected, actual.toString(), false);
    }

    @Test
    void testConvertTypeDecimal() {
        final int precision = 5;
        final int scale = 3;
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createDecimal(precision, scale));
        assertAll(() -> assertTypeName(jsonObject, "decimal"),
                () -> assertThat(jsonObject.getInt("precision"), equalTo(precision)),
                () -> assertThat(jsonObject.getInt("scale"), equalTo(scale)));
    }

    public void assertTypeName(final JsonObject jsonObject, final String name) {
        assertThat(jsonObject.getString("type"), equalTo(name));
    }

    @Test
    void testConvertTypeTimestamp() {
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createTimestamp(true));
        assertAll(() -> assertTypeName(jsonObject, "timestamp"),
                () -> assertThat(jsonObject.getBoolean("withLocalTimeZone"), equalTo(true)));
    }

    @Test
    void testConvertTypeGeometry() {
        final int srid = 2;
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createGeometry(srid));
        assertAll(() -> assertTypeName(jsonObject, "geometry"),
                () -> assertThat(jsonObject.getInt("srid"), equalTo(srid)));
    }

    @Test
    void testConvertTypeHashtype() {
        final int bytesize = 16;
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createHashtype(bytesize));
        assertAll(() -> assertTypeName(jsonObject, "hashtype"),
              () -> assertThat(jsonObject.getInt("bytesize"), equalTo(bytesize)));
    }

    @Test
    void testConvertTypeIntervalDayToSecond() {
        final int precision = 2;
        final int fraction = 3;
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createIntervalDaySecond(precision, fraction));
        assertAll(() -> assertTypeName(jsonObject, "interval"),
                () -> assertThat(jsonObject.getString("fromTo"), equalTo("DAY TO SECONDS")),
                () -> assertThat(jsonObject.getInt("precision"), equalTo(precision)),
                () -> assertThat(jsonObject.getInt("fraction"), equalTo(fraction)));
    }

    @Test
    void testConvertTypeIntervalYearToMonths() {
        final int precision = 2;
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createIntervalYearMonth(precision));
        assertAll(() -> assertTypeName(jsonObject, "interval"),
                () -> assertThat(jsonObject.getString("fromTo"), equalTo("YEAR TO MONTH")),
                () -> assertThat(jsonObject.getInt("precision"), equalTo(precision)));
    }

    @CsvSource({ "42,ASCII,ASCII", "3000,UTF8,UTF8" })
    @ParameterizedTest
    void testConvertTypeVarChar(final int size, final ExaCharset characterSet, final String characterSetName) {
        final JsonObject jsonObject = CONVERTER.convertType(DataType.createVarChar(size, characterSet));
        assertAll(() -> assertTypeName(jsonObject, "varchar"),
                () -> assertThat(jsonObject.getInt("size"), equalTo(size)),
                () -> assertThat(jsonObject.getString("characterSet"), equalTo(characterSetName)));
    }

    @Test
    void testConvertTypeUnsupportedThrowsException() {
        final DataType dataType = Mockito.mock(DataType.class);
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.UNSUPPORTED);
        assertThrows(IllegalArgumentException.class, () -> CONVERTER.convertType(dataType));
    }
}