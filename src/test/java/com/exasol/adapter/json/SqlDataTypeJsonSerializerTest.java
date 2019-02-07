package com.exasol.adapter.json;

import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.json.JsonObject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SqlDataTypeJsonSerializerTest {
    @Mock
    DataType dataType;

    @Test
    void testSerializeUnsupported() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.UNSUPPORTED);
        assertThrows(IllegalArgumentException.class, () -> SqlDataTypeJsonSerializer.serialize(dataType));
    }

    @Test
    void testSerializeDecimal() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DECIMAL);
        when(dataType.getPrecision()).thenReturn(5);
        when(dataType.getScale()).thenReturn(3);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(dataType).build();
        assertThat(jsonObject.getInt("precision"), equalTo(5));
        assertThat(jsonObject.getInt("scale"), equalTo(3));
    }

    @Test
    void testSerializeDoubleDateAndBoolean() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DOUBLE);
        assertAll(() -> SqlDataTypeJsonSerializer.serialize(dataType));
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DATE);
        assertAll(() -> SqlDataTypeJsonSerializer.serialize(dataType));
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.BOOLEAN);
        assertAll(() -> SqlDataTypeJsonSerializer.serialize(dataType));
    }

    @Test
    void testSerializeVarcharAndChar() {
        serializeCharOrVarchar(DataType.ExaDataType.CHAR);
        serializeCharOrVarchar(DataType.ExaDataType.VARCHAR);
    }

    private void serializeCharOrVarchar(final DataType.ExaDataType exaDataType) {
        when(dataType.getExaDataType()).thenReturn(exaDataType);
        when(dataType.getSize()).thenReturn(2);
        when(dataType.getCharset()).thenReturn(DataType.ExaCharset.UTF8);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(dataType).build();
        assertThat(jsonObject.getInt("size"), equalTo(2));
        assertThat(jsonObject.getString("characterSet"), equalTo("UTF8"));
    }

    @Test
    void testSerializeTimestamp() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.TIMESTAMP);
        when(dataType.isWithLocalTimezone()).thenReturn(true);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(dataType).build();
        assertThat(jsonObject.getBoolean("withLocalTimeZone"), equalTo(true));
    }

    @Test
    void testSerializeGeometry() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.GEOMETRY);
        when(dataType.getGeometrySrid()).thenReturn(2);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(dataType).build();
        assertThat(jsonObject.getInt("srid"), equalTo(2));
    }

    @Test
    void testSerializeInterval() {
        when(dataType.getExaDataType()).thenReturn(DataType.ExaDataType.INTERVAL);
        when(dataType.getIntervalType()).thenReturn(DataType.IntervalType.DAY_TO_SECOND);
        when(dataType.getIntervalFraction()).thenReturn(3);
        when(dataType.getPrecision()).thenReturn(2);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(dataType).build();
        assertThat(jsonObject.getString("fromTo"), equalTo("DAY TO SECONDS"));
        assertThat(jsonObject.getInt("precision"), equalTo(2));
        assertThat(jsonObject.getInt("fraction"), equalTo(3));
    }
}