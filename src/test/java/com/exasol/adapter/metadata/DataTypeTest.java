package com.exasol.adapter.metadata;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class DataTypeTest {
    @Test
    void createDecimal() {
        final DataType dataType = DataType.createDecimal(10, 2);
        assertAll(() -> assertThat(dataType.getPrecision(), equalTo(10)),
              () -> assertThat(dataType.getScale(), equalTo(2)),
              () -> assertThat(dataType.toString(), equalTo("DECIMAL(10, 2)")));
    }

    @Test
    void createDouble() {
        final DataType dataType = DataType.createDouble();
        assertThat(dataType.toString(), equalTo("DOUBLE"));
    }

    @Test
    void createVarChar() {
        final DataType dataType = DataType.createVarChar(100, DataType.ExaCharset.UTF8);
        assertAll(() -> assertThat(dataType.getCharset(), equalTo(DataType.ExaCharset.UTF8)),
              () -> assertThat(dataType.getSize(), equalTo(100)),
              () -> assertThat(dataType.toString(), equalTo("VARCHAR(100) UTF8")));
    }

    @Test
    void createChar() {
        final DataType dataType = DataType.createChar(101, DataType.ExaCharset.UTF8);
        assertAll(() -> assertThat(dataType.getCharset(), equalTo(DataType.ExaCharset.UTF8)),
              () -> assertThat(dataType.getSize(), equalTo(101)),
              () -> assertThat(dataType.toString(), equalTo("CHAR(101) UTF8")));
    }

    @Test
    void createTimestampWithLocalTimezone() {
        final DataType dataType = DataType.createTimestamp(true);
        assertAll(() -> assertTrue(dataType.isWithLocalTimezone()),
              () -> assertThat(dataType.toString(), equalTo("TIMESTAMP WITH LOCAL TIME ZONE")));
    }

    @Test
    void createTimestampWithoutLocalTimezone() {
        final DataType dataType = DataType.createTimestamp(false);
        assertAll(() -> assertFalse(dataType.isWithLocalTimezone()),
              () -> assertThat(dataType.toString(), equalTo("TIMESTAMP")));
    }

    @Test
    void createBool() {
        final DataType dataType = DataType.createBool();
        assertThat(dataType.toString(), equalTo("BOOLEAN"));
    }

    @Test
    void createDate() {
        final DataType dataType = DataType.createDate();
        assertThat(dataType.toString(), equalTo("DATE"));
    }

    @Test
    void createGeometry() {
        final DataType dataType = DataType.createGeometry(4);
        assertAll(() -> assertThat(dataType.getGeometrySrid(), equalTo(4)),
              () -> assertThat(dataType.toString(), equalTo("GEOMETRY(4)")));
    }

    @Test
    void createIntervalDaySecond() {
        final DataType dataType = DataType.createIntervalDaySecond(10, 2);
        assertAll(() -> assertThat(dataType.getPrecision(), equalTo(10)),
              () -> assertThat(dataType.getIntervalFraction(), equalTo(2)),
              () -> assertThat(dataType.toString(), equalTo("INTERVAL DAY (10) TO SECOND (2)")));
    }

    @Test
    void createIntervalYearMonth() {
        final DataType dataType = DataType.createIntervalYearMonth(10);
        assertAll(() -> assertThat(dataType.getPrecision(), equalTo(10)),
              () -> assertThat(dataType.toString(), equalTo("INTERVAL YEAR (10) TO MONTH")));
    }
}