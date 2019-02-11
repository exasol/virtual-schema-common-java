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
        this.sqlLiteralBoolTrue = new SqlLiteralBool(true);
        this.sqlLiteralBoolFalse = new SqlLiteralBool(false);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralBoolTrue.getValue(), equalTo(true));
        assertThat(this.sqlLiteralBoolFalse.getValue(), equalTo(false));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralBoolTrue.toSimpleSql(), equalTo("true"));
        assertThat(this.sqlLiteralBoolFalse.toSimpleSql(), equalTo("false"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralBoolTrue.getType(), equalTo(SqlNodeType.LITERAL_BOOL));
    }
}