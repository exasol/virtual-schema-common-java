package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralTimestampTest {
    private static final String VALUE = "2019-02-07";
    private SqlLiteralTimestamp sqlLiteralDate;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDate = new SqlLiteralTimestamp(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralDate.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralDate.toSimpleSql(), equalTo("TIMESTAMP '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralDate.getType(), equalTo(SqlNodeType.LITERAL_TIMESTAMP));
    }
}