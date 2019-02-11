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
        this.sqlLiteralTimestampUtc = new SqlLiteralTimestampUtc(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralTimestampUtc.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralTimestampUtc.toSimpleSql(), equalTo("TIMESTAMP '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralTimestampUtc.getType(),
              equalTo(SqlNodeType.LITERAL_TIMESTAMPUTC));
    }
}