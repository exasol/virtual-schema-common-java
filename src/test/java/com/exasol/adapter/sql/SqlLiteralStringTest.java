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
        sqlLiteralString = new SqlLiteralString(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(sqlLiteralString.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralString.toSimpleSql(), equalTo("'" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralString.getType(), equalTo(SqlNodeType.LITERAL_STRING));
    }
}