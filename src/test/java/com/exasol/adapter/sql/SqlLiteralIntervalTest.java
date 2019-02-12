package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlLiteralIntervalTest {
    private static final String VALUE = "5";
    private SqlLiteralInterval sqlLiteralIntervalDayToSecond;
    private SqlLiteralInterval sqlLiteralIntervalYearToMonth;
    private DataType dayToSecond;
    private DataType yearToMonth;

    @BeforeEach
    void setUp() {
        this.dayToSecond = DataType.createIntervalDaySecond(1, 2);
        this.yearToMonth = DataType.createIntervalYearMonth(3);
        this.sqlLiteralIntervalDayToSecond = new SqlLiteralInterval(VALUE, this.dayToSecond);
        this.sqlLiteralIntervalYearToMonth = new SqlLiteralInterval(VALUE, this.yearToMonth);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSqlDayToSecond() {
        assertThat(this.sqlLiteralIntervalDayToSecond.toSimpleSql(),
              equalTo("INTERVAL '" + VALUE + "' DAY (1) TO SECOND (2)"));
    }

    @Test
    void testToSimpleSqlYearToMonth() {
        assertThat(this.sqlLiteralIntervalYearToMonth.toSimpleSql(),
              equalTo("INTERVAL '" + VALUE + "' YEAR (3) TO MONTH"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getType(),
              equalTo(SqlNodeType.LITERAL_TIMESTAMP));
    }

    @Test
    void testGetDataType() {
        assertThat(this.sqlLiteralIntervalDayToSecond.getDataType(), equalTo(this.dayToSecond));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralInterval> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlLiteralIntervalDayToSecond)).thenReturn(
              this.sqlLiteralIntervalDayToSecond);
        assertThat(this.sqlLiteralIntervalDayToSecond.accept(visitor), equalTo(
              this.sqlLiteralIntervalDayToSecond));
    }
}