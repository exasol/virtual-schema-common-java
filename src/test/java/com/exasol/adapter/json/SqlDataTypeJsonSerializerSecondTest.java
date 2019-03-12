package com.exasol.adapter.json;

import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.json.JsonObject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqlDataTypeJsonSerializerSecondTest {
    @Mock
    DataType dataType;

    @Test
    void testSerializeUnsupportedThrowsException() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.UNSUPPORTED);
        assertThrows(IllegalArgumentException.class, () -> SqlDataTypeJsonSerializer.serialize(this.dataType));
    }

    @Test
    void testSerializeDecimal() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DECIMAL);
        when(this.dataType.getPrecision()).thenReturn(5);
        when(this.dataType.getScale()).thenReturn(3);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(this.dataType).build();
        assertAll(() -> assertEquals(5, jsonObject.getInt("precision")),
              () -> assertEquals(3, jsonObject.getInt("scale")));
    }

    @Test
    void testSerializeDoubleNoException() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DOUBLE);
        SqlDataTypeJsonSerializer.serialize(this.dataType);
    }

    @Test
    void testSerializeDateNoException() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.DATE);
        SqlDataTypeJsonSerializer.serialize(this.dataType);
    }

    @Test
    void testSerializeBooleanNoException() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.BOOLEAN);
        SqlDataTypeJsonSerializer.serialize(this.dataType);
    }

    @Test
    void testSerializeVarcharNoException() {
        serializeCharOrVarchar(DataType.ExaDataType.VARCHAR);
    }

    @Test
    void testSerializeCharNoException() {
        serializeCharOrVarchar(DataType.ExaDataType.CHAR);
    }

    private void serializeCharOrVarchar(final DataType.ExaDataType exaDataType) {
        when(this.dataType.getExaDataType()).thenReturn(exaDataType);
        when(this.dataType.getSize()).thenReturn(2);
        when(this.dataType.getCharset()).thenReturn(DataType.ExaCharset.UTF8);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(this.dataType).build();
        assertThat(jsonObject.getInt("size"), equalTo(2));
        assertThat(jsonObject.getString("characterSet"), equalTo("UTF8"));
    }

    @Test
    void testSerializeTimestamp() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.TIMESTAMP);
        when(this.dataType.isWithLocalTimezone()).thenReturn(true);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(this.dataType).build();
        assertThat(jsonObject.getBoolean("withLocalTimeZone"), equalTo(true));
    }

    @Test
    void testSerializeGeometry() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.GEOMETRY);
        when(this.dataType.getGeometrySrid()).thenReturn(2);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(this.dataType).build();
        assertThat(jsonObject.getInt("srid"), equalTo(2));
    }

    @Test
    void testSerializeInterval() {
        when(this.dataType.getExaDataType()).thenReturn(DataType.ExaDataType.INTERVAL);
        when(this.dataType.getIntervalType()).thenReturn(DataType.IntervalType.DAY_TO_SECOND);
        when(this.dataType.getIntervalFraction()).thenReturn(3);
        when(this.dataType.getPrecision()).thenReturn(2);
        final JsonObject jsonObject = SqlDataTypeJsonSerializer.serialize(this.dataType).build();
        assertThat(jsonObject.getString("fromTo"), equalTo("DAY TO SECONDS"));
        assertThat(jsonObject.getInt("precision"), equalTo(2));
        assertThat(jsonObject.getInt("fraction"), equalTo(3));
    }
}