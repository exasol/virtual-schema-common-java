package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import com.exasol.mocking.MockUtils;

class SqlLiteralIntervalTest {
    private static final String VALUE = "5";
    private SqlLiteralInterval sqlLiteralIntervalDayToSecond;
    private DataType dayToSecond;

    @BeforeEach
    void setUp() {
        this.dayToSecond = DataType.createIntervalDaySecond(1, 2);
        final DataType yearToMonth = DataType.createIntervalYearMonth(3);
        this.sqlLiteralIntervalDayToSecond = new SqlLiteralInterval(VALUE, this.dayToSecond);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getValue(), equalTo(VALUE));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getType(), equalTo(SqlNodeType.LITERAL_INTERVAL));
    }

    @Test
    void testGetDataType() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getDataType(), equalTo(this.dayToSecond));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralInterval> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlLiteralIntervalDayToSecond)).thenReturn(this.sqlLiteralIntervalDayToSecond);
        assertThat(this.sqlLiteralIntervalDayToSecond.accept(visitor), equalTo(this.sqlLiteralIntervalDayToSecond));
    }
}