package com.exasol.adapter.sql;

import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralIntervalTest {
    private static final String VALUE = "2019-02-07";
    private SqlLiteralInterval sqlLiteralIntervalDayToSecond;
    private SqlLiteralInterval sqlLiteralIntervalYearToMonth;
    private DataType dayToSecond;
    private DataType yearToMonth;

    @BeforeEach
    void setUp() {
        dayToSecond = DataType.createIntervalDaySecond(1, 2);
        yearToMonth = DataType.createIntervalYearMonth(3);
        sqlLiteralIntervalDayToSecond = new SqlLiteralInterval(VALUE, dayToSecond);
        sqlLiteralIntervalYearToMonth = new SqlLiteralInterval(VALUE, yearToMonth);
    }

    @Test
    void testGetValue() {
        assertThat(sqlLiteralIntervalDayToSecond.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralIntervalDayToSecond.toSimpleSql(), equalTo("INTERVAL '" + VALUE + "' DAY (1) TO SECOND (2)"));
        assertThat(sqlLiteralIntervalYearToMonth.toSimpleSql(), equalTo("INTERVAL '" + VALUE + "' YEAR (3) TO MONTH"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralIntervalDayToSecond.getType(), equalTo(SqlNodeType.LITERAL_TIMESTAMP));
    }

    @Test
    void testGetDataType() {
        assertThat(sqlLiteralIntervalDayToSecond.getDataType(), equalTo(dayToSecond));
    }
}