package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralTimestampUtcTest {
    private static final String VALUE = "2019-02-07";
    private SqlLiteralTimestampUtc sqlLiteralTimestampUtc;

    @BeforeEach
    void setUp() {
        sqlLiteralTimestampUtc = new SqlLiteralTimestampUtc(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(sqlLiteralTimestampUtc.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralTimestampUtc.toSimpleSql(), equalTo("TIMESTAMP '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralTimestampUtc.getType(), equalTo(SqlNodeType.LITERAL_TIMESTAMPUTC));
    }
}