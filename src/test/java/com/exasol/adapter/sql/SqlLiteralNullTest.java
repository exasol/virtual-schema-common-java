package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlLiteralNullTest {
    private SqlLiteralNull sqlLiteralNull;

    @BeforeEach
    void setUp() {
        this.sqlLiteralNull = new SqlLiteralNull();
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralNull.toSimpleSql(), equalTo("NULL"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralNull.getType(), equalTo(SqlNodeType.LITERAL_NULL));
    }
}