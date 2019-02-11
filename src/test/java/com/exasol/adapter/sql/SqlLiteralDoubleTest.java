package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralDoubleTest {
    private static final Double VALUE = 20.1;
    private SqlLiteralDouble sqlLiteralDouble;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDouble = new SqlLiteralDouble(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralDouble.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralDouble.toSimpleSql(), equalTo("20.1"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralDouble.getType(), equalTo(SqlNodeType.LITERAL_DOUBLE));
    }
}