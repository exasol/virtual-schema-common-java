package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralNullTest {
    private SqlLiteralNull sqlLiteralNull;

    @BeforeEach
    void setUp() {
        sqlLiteralNull = new SqlLiteralNull();
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlLiteralNull.toSimpleSql(), equalTo("NULL"));
    }

    @Test
    void testGetType() {
        assertThat(sqlLiteralNull.getType(), equalTo(SqlNodeType.LITERAL_NULL));
    }

}