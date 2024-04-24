package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.json.builder.JsonEntry.*;
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
import com.exasol.adapter.request.parser.json.builder.JsonEntry;
import com.exasol.adapter.request.parser.json.builder.JsonParent;

import jakarta.json.*;

class DataTypeParserTest {

    @ParameterizedTest
    @ValueSource(strings = { "UNKNOWN", "abcdef" })
    @NullSource
    void unknownDatatype_ThrowsException(final String name) {
        final JsonEntry data = JsonEntry.array(object(entry("type", name)));
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(data));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-37: Unsupported datatype '" + name + "'"));
    }

    @Test
    void missingType_ThrowsException() {
        final JsonEntry data = JsonEntry.array(object(entry("no_type", "DECIMAL")));
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(data));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-40: Unspecified datatype"));
    }

    @Test
    void varchar() {
        verifySingle(DataType.createVarChar(21, ExaCharset.ASCII), object( //
                entry("type", "VARCHAR"), //
                entry("size", 21), //
                entry("characterSet", "ASCII")));
    }

    @Test
    void testChar() {
        verifySingle(DataType.createChar(22, ExaCharset.UTF8), object( //
                entry("type", "CHAR"), //
                entry("size", 22), //
                entry("characterSet", "UTF8")));
    }

    @Test
    void decimal() {
        verifySingle(DataType.createDecimal(31, 41), object( //
                entry("type", "DECIMAL"), //
                entry("precision", 31), //
                entry("scale", 41)));
    }

    @Test
    void testDouble() {
        verifySingle(DataType.createDouble(), object(entry("type", "DOUBLE")));
    }

    @Test
    void date() {
        verifySingle(DataType.createDate(), object(entry("type", "DATE")));
    }

    @Test
    void timestamp() {
        verifySingle(DataType.createTimestamp(true, 3), object( //
                entry("type", "TIMESTAMP"), //
                entry("withLocalTimeZone", true)));
    }

    @Test
    void timestampWithoutLocalTimeZone() {
        verifySingle(DataType.createTimestamp(true, 3), object( //
                entry("type", "TIMESTAMP"), //
                entry("withLocalTimeZone", true)));
    }

    @Test
    void timestampMissingLocalTimeZoneProperty() {
        verifySingle(DataType.createTimestamp(false, 3), object( //
                entry("type", "TIMESTAMP")));
    }

    @Test
    void timestampWithCustomPrecision() {
        verifySingle(DataType.createTimestamp(true, 6), object( //
                entry("type", "TIMESTAMP"), //
                entry("withLocalTimeZone", true), //
                entry("fractionalSecondsPrecision", 6)));
    }

    @Test
    void testBoolean() {
        verifySingle(DataType.createBool(), object( //
                entry("type", "BOOLEAN")));
    }

    @Test
    void geometry() {
        verifySingle(DataType.createGeometry(42), object( //
                entry("type", "GEOMETRY"), //
                entry("srid", 42)));
    }

    @Test
    void interval() {
        verifySingle(DataType.createIntervalDaySecond(32, 51), object( //
                entry("type", "INTERVAL"), //
                entry("fromTo", "DAY TO SECONDS"), //
                entry("precision", 32), //
                entry("fraction", 51)));
        verifySingle(DataType.createIntervalYearMonth(32), object( //
                entry("type", "INTERVAL"), //
                entry("fromTo", "YEAR TO MONTH"), //
                entry("precision", 32), //
                entry("fraction", 51))); // note: parser ignores extra properties
    }

    @Test
    void hashtype() {
        verifySingle(DataType.createHashtype(2031), object( //
                entry("type", "HASHTYPE"), //
                entry("bytesize", 2031)));
    }

    // ----------------------------------------------
    // default values for data type properties

    @Test
    void defaultCharacterSet_Utf8() {
        verifySingle(DataType.createVarChar(21, ExaCharset.UTF8), object( //
                entry("type", "VARCHAR"), //
                entry("size", 21)));
        verifySingle(DataType.createChar(22, ExaCharset.UTF8), object( //
                entry("type", "CHAR"), //
                entry("size", 22)));
    }

    @Test
    void timestampDefaultLocalTimezone_False() {
        verifySingle(DataType.createTimestamp(false, 3), object(entry("type", "TIMESTAMP")));
    }

    @Test
    void timestampDefaultPrecision_3() {
        verifySingle(DataType.createTimestamp(false, 3), object(entry("type", "TIMESTAMP")));
    }

    @Test
    void geometryDefaultSri_0() {
        verifySingle(DataType.createGeometry(0), object(entry("type", "GEOMETRY")));
    }

    @Test
    void intervalDefaultPrecision_2() {
        verifySingle(DataType.createIntervalDaySecond(2, 51), object( //
                entry("type", "INTERVAL"), //
                entry("fromTo", "DAY TO SECONDS"), //
                entry("fraction", 51)));
    }

    @Test
    void intervalDefaultFraction_3() {
        verifySingle(DataType.createIntervalDaySecond(32, 3), object( //
                entry("type", "INTERVAL"), //
                entry("fromTo", "DAY TO SECONDS"), //
                entry("precision", 32)));
    }

    @Test
    void hashTypeDefaultByteSize_16() {
        verifySingle(DataType.createHashtype(16), object(entry("type", "HASHTYPE")));
    }

    @Test
    void missingRequiredProperty_ThrowsException() {
        verifyMissingRequiredProperty(object(entry("type", "DECIMAL")), "DECIMAL", "");
        verifyMissingRequiredProperty(object(entry("type", "CHAR")), "CHAR", "size");
        verifyMissingRequiredProperty(object(entry("type", "VARCHAR")), "VARCHAR", "size");
    }

    @Test
    void illegalPropertyValue_ThrowsException() {
        verifyIllegalPropertyValue(object( //
                entry("type", "VARCHAR"), //
                entry("size", 20), //
                entry("characterSet", "xxx")), //
                "VARCHAR", "characterSet", "\"xxx\"");
        verifyIllegalPropertyValue("VARCHAR", "size", "abc");
        verifyIllegalPropertyValue("TIMESTAMP", "withLocalTimeZone", 123);
        verifyIllegalPropertyValue("HASHTYPE", "bytesize", true);
        verifyIllegalPropertyValue("GEOMETRY", "srid", false);
        verifyIllegalPropertyValue("INTERVAL", "precision", "p");
        verifyIllegalPropertyValue(object(entry("type", "INTERVAL"), //
                entry("fromTo", "DAY TO SECONDS"), //
                entry("precision", 5), //
                entry("fraction", true)), //
                "INTERVAL", "fraction", "true");
        verifyIllegalPropertyValue(object(entry("type", "INTERVAL"), //
                entry("fromTo", "xxx")), //
                "INTERVAL", "fromTo", "\"xxx\"");
    }

    @Test
    void multipleDatatypes() {
        final JsonParent builder = JsonEntry.array( //
                object(entry("type", "VARCHAR"), //
                        entry("size", 21), //
                        entry("characterSet", "ASCII")), //
                object(entry("type", "CHAR"), //
                        entry("size", 22), //
                        entry("characterSet", "UTF8")), //
                object(entry("type", "DECIMAL"), //
                        entry("precision", 31), //
                        entry("scale", 41)), //
                object(entry("type", "DOUBLE")), //
                object(entry("type", "DATE")), //
                object(entry("type", "TIMESTAMP"), //
                        entry("withLocalTimeZone", true), entry("fractionalSecondsPrecision", 4)), //
                object(entry("type", "BOOLEAN")), //
                object(entry("type", "GEOMETRY"), //
                        entry("srid", 42)), //
                object(entry("type", "INTERVAL"), //
                        entry("fromTo", "DAY TO SECONDS"), //
                        entry("precision", 32), //
                        entry("fraction", 51)), //
                object(entry("type", "HASHTYPE")));
        final List<DataType> expected = List.of( //
                DataType.createVarChar(21, ExaCharset.ASCII), //
                DataType.createChar(22, ExaCharset.UTF8), //
                DataType.createDecimal(31, 41), //
                DataType.createDouble(), //
                DataType.createDate(), //
                DataType.createTimestamp(true, 4), //
                DataType.createBool(), //
                DataType.createGeometry(42), //
                DataType.createIntervalDaySecond(32, 51), //
                DataType.createHashtype(16));
        assertThat(parse(builder), equalTo(expected));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final String value) {
        verifyIllegalPropertyValue(object(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf("\"" + value + "\""));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final boolean value) {
        verifyIllegalPropertyValue(object(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf(value));
    }

    private void verifyIllegalPropertyValue(final String datatype, final String property, final int value) {
        verifyIllegalPropertyValue(object(entry("type", datatype), entry(property, value)), datatype, property,
                String.valueOf(value));
    }

    private void verifyIllegalPropertyValue(final JsonParent builder, final String datatype, final String property,
            final String value) {
        final JsonEntry data = array(builder);
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(data));
        assertThat(e.getMessage(), startsWith("E-VSCOMJAVA-39: Datatype '" + datatype + "', property '" + property
                + "': Illegal value " + value + "."));
    }

    // --------------------------------------------
    // test utilities

    private void verifyMissingRequiredProperty(final JsonParent builder, final String datatype, final String property) {
        final JsonEntry data = array(builder);
        final Exception e = assertThrows(DataTypeParserException.class, () -> parse(data));
        assertThat(e.getMessage(),
                startsWith("E-VSCOMJAVA-36: Datatype '" + datatype + "': Missing property '" + property));
    }

    private void verifySingle(final DataType expected, final JsonEntry builder) {
        final DataType actual = parse(array(builder)).get(0);
        assertThat(actual, equalTo(expected));
    }

    private List<DataType> parse(final JsonEntry builder) {
        return DataTypeParser.create().parse(readArray(builder.render()));
    }

    private JsonArray readArray(final String string) {
        try (final JsonReader jsonReader = Json.createReader(new StringReader(string))) {
            return jsonReader.readArray();
        }
    }
}
