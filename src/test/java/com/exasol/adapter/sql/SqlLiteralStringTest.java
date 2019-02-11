package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralStringTest {
    private static final String VALUE = "test string";
    private SqlLiteralString sqlLiteralString;

    @BeforeEach
    void setUp() {
        this.sqlLiteralString = new SqlLiteralString(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralString.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralString.toSimpleSql(), equalTo("'" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralString.getType(), equalTo(SqlNodeType.LITERAL_STRING));
    }
}