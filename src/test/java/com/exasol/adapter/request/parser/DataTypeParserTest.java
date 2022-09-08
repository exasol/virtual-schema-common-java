package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.json.JsonEntry.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.request.parser.DataTypeParser.DataTypeParserException;
import com.exasol.adapter.request.parser.json.JsonEntry;
import com.exasol.adapter.request.parser.json.JsonParent;

import jakarta.json.*;

class DataTypeParserTest {

    @ParameterizedTest
    @ValueSource(strings = { "UNKNOWN", "abcdef" })
    @NullSource
    void unknownDatatype_ThrowsException(final String name) {
        final Exception e = assertThrows(DataTypeParserException.class,
                () -> parse(JsonEntry.array(group(entry("type", name)))));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-37: Unsupported datatype '" + name + "'"));
    }

    @Test
    void missingType_ThrowsException() {
        final Exception e = assertThrows(DataTypeParserException.class,
                () -> parse(JsonEntry.array(group(entry("no_type", "DECIMAL")))));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-40: Unspecified datatype"));
    }

    @Test
    void varchar() {
        verifySingle(DataType.createVarChar(21, ExaCharset.ASCII), group( //
                entry("type", "VARCHAR"), //
                entry("size", 21), //
                entry("characterSet", "ASCII")));
    }

    @Test
    void testChar() {
        verifySingle(DataType.createChar(22, ExaCharset.UTF8), group( //
                entry("type", "CHAR"), //
                entry("size", 22), //
                entry("characterSet", "UTF8")));
    }

    @Test
    void decimal() {
        verifySingle(DataType.createDecimal(31, 41), group( //
                entry("type", "DECIMAL"), //
                entry("precision", 31), //
                entry("scale", 41)));
    }

    @Test
    void testDouble() {
        verifySingle(DataType.createDouble(), group(entry("type", "DOUBLE")));
    }

    @Test
    void date() {
        verifySingle(DataType.createDate(), group(entry("type", "DATE")));
    }

    @Test
    void timestamp() {
        verifySingle(DataType.createTimestamp(true), group( //
                entry("type", "TIMESTAMP"), //
                entry("withlocaltimezone", true)));
    }

    @Test
    void testBoolean() {
        verifySingle(DataType.createBool(), group( //
                entry("type", "BOOLEAN")));
    }

    @Test
    void geometry() {
        verifySingle(DataType.createGeometry(42), group( //
                entry("type", "GEOMETRY"), //
                entry("scale", 42)));
    }

    @Test
    void interval() {
        verifySingle(DataType.createIntervalDaySecond(32, 51), group( //
                entry("type", "INTERVAL"), //
                entry("precision", 32), //
                entry("fraction", 51)));
    }

    @Test
    void hashtype() {
        verifySingle(DataType.createHashtype(2031), group( //
                entry("type", "HASHTYPE"), //
                entry("byteSize", 2031)));
    }

    // ----------------------------------------------
    // default values for data type properties

    @Test
    void defaultCharacterSet_Utf8() {
        verifySingle(DataType.createVarChar(21, ExaCharset.UTF8), group( //
                entry("type", "VARCHAR"), //
                entry("size", 21)));
        verifySingle(DataType.createChar(22, ExaCharset.UTF8), group( //
                entry("type", "CHAR"), //
                entry("size", 22)));
    }

    @Test
    void timestampDefaultLocalTimezone_False() {
        verifySingle(DataType.createTimestamp(false), group(entry("type", "TIMESTAMP")));
    }

    @Test
    void geometryDefaultSri_0() {
        verifySingle(DataType.createGeometry(0), group(entry("type", "GEOMETRY")));
    }

    @Test
    void intervalDefaultPrecision_2() {
        verifySingle(DataType.createIntervalDaySecond(2, 51), group( //
                entry("type", "INTERVAL"), //
                entry("fraction", 51)));
    }

    @Test
    void intervalDefaultFraction_3() {
        verifySingle(DataType.createIntervalDaySecond(32, 3), group( //
                entry("type", "INTERVAL"), //
                entry("precision", 32)));
    }

    @Test
    void hashTypeDefaultByteSize_16() {
        verifySingle(DataType.createHashtype(16), group(entry("type", "HASHTYPE")));
    }

    @Test
    void missingRequiredProperty_ThrowsException() {
        verifyMissingRequiredProperty(group(entry("type", "DECIMAL")), "DECIMAL", "");
        verifyMissingRequiredProperty(group(entry("type", "CHAR")), "CHAR", "size");
        verifyMissingRequiredProperty(group(entry("type", "VARCHAR")), "VARCHAR", "size");
    }

    @Test
    void illegalPropertyValue_ThrowsException() {
        verifyIllegalPropertyValue(group( //
                entry("type", "VARCHAR"), //
                entry("size", 20), //
                entry("characterSet", "xxx")), //
                "VARCHAR", "characterSet", "\"xxx\"");
        verifyIllegalPropertyValue("VARCHAR", "size", "abc");
        verifyIllegalPropertyValue("TIMESTAMP", "withlocaltimezone", 123);
        verifyIllegalPropertyValue("HASHTYPE", "byteSize", true);
        verifyIllegalPropertyValue("GEOMETRY", "scale", false);
        verifyIllegalPropertyValue("INTERVAL", "precision", "p");
        verifyIllegalPropertyValue(group(entry("type", "INTERVAL"), //
                entry("precision", 5), //
                entry("fraction", true)), //
                "INTERVAL", "fraction", "true");
    }

    @Test
    void multipleDatatypes() {
        final JsonParent builder = JsonEntry.array( //
                group(entry("type", "VARCHAR"), //
                        entry("size", 21), //
                        entry("characterSet", "ASCII")), //
                group(entry("type", "CHAR"), //
                        entry("size", 22), //
                        entry("characterSet", "UTF8")), //
                group(entry("type", "DECIMAL"), //
                        entry("precision", 31), //
                        entry("scale", 41)), //
                group(entry("type", "DOUBLE")), //
                group(entry("type", "DATE")), //
                group(entry("type", "TIMESTAMP"), //
                        entry("withlocaltimezone", true)), //
                group(entry("type", "BOOLEAN")), //
                group(entry("type", "GEOMETRY"), //
                        entry("scale", 42)), //
                group(entry("type", "INTERVAL"), //
                        entry("precision", 32), //
                        entry("fraction", 51)), //
                group(entry("type", "HASHTYPE")));
        final List<DataType> expected = List.of( //
                DataType.createVarChar(21, ExaCharset.ASCII), //
                DataType.createChar(22, ExaCharset.UTF8), //
                DataType.createDecimal(31, 41), //
                DataType.createDouble(), //
                DataType.createDate(), //
                DataType.createTimestamp(true), //
                DataType.createBool(), //
                DataType.createGeometry(42), //
                DataType.createIntervalDaySecond(32, 51), //
                DataType.createHashtype(16));
        assertThat(parse(builder), equalTo(expected));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final String value) {
        verifyIllegalPropertyValue(group(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf("\"" + value + "\""));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final boolean value) {
        verifyIllegalPropertyValue(group(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf(value));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final int value) {
        verifyIllegalPropertyValue(group(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf(value));
    }

    private void verifyIllegalPropertyValue(final JsonParent builder, final String datatype, final String property,
            final String value) {
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(array(builder)));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-40: Datatype '" + datatype + "', property '" + property
                + "': Illegal value " + value + "."));
    }

    // --------------------------------------------
    // test utilities

    private void verifyMissingRequiredProperty(final JsonParent builder, final String datatype, final String property) {
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(array(builder)));
        assertThat(e.getMessage(),
                startsWith("E-VSCOMJAVA-36: Datatype '" + datatype + "': Missing property '" + property));
    }

    private void verifySingle(final DataType expected, final JsonEntry builder) {
        final DataType actual = parse(array(builder)).get(0);
        assertThat(actual, equalTo(expected));
    }

    private List<DataType> parse(final JsonParent builder) {
        return DataTypeParser.create().parse(readArray(builder.render()));
    }

    private JsonArray readArray(final String string) {
        try (final JsonReader jsonReader = Json.createReader(new StringReader(string))) {
            return jsonReader.readArray();
        }
    }
}
