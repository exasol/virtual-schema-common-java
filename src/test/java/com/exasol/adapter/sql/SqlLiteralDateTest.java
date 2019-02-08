package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralDateTest {
    private static final String VALUE = "2019-02-07";
    private SqlLiteralDate sqlLiteralDate;

    @BeforeEach
    void setUp() {
        sqlLiteralDate = new SqlLiteralDate(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(sqlLiteralDate.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralDate.toSimpleSql(), equalTo("DATE '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralDate.getType(), equalTo(SqlNodeType.LITERAL_DATE));
    }
}