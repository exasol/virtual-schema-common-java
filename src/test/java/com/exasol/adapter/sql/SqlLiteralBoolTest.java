package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralBoolTest {
    private SqlLiteralBool sqlLiteralBoolTrue;
    private SqlLiteralBool sqlLiteralBoolFalse;

    @BeforeEach
    void setUp() {
        sqlLiteralBoolTrue = new SqlLiteralBool(true);
        sqlLiteralBoolFalse = new SqlLiteralBool(false);
    }

    @Test
    void testGetValue() {
        assertThat(sqlLiteralBoolTrue.getValue(), equalTo(true));
        assertThat(sqlLiteralBoolFalse.getValue(), equalTo(false));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralBoolTrue.toSimpleSql(), equalTo("true"));
        assertThat(sqlLiteralBoolFalse.toSimpleSql(), equalTo("false"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralBoolTrue.getType(), equalTo(SqlNodeType.LITERAL_BOOL));
    }
}