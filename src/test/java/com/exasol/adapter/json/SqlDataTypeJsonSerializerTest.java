package com.exasol.adapter.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.metadata.DataType;

@ExtendWith(MockitoExtension.class)
class SqlDataTypeJsonSerializerTest {
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