package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlPredicateNotTest {
    private SqlPredicateNot sqlPredicateNot;
    private SqlLiteralNull sqlLiteralNull;

    @BeforeEach
    void setUp() {
        sqlLiteralNull = new SqlLiteralNull();
        sqlPredicateNot = new SqlPredicateNot(sqlLiteralNull);
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlPredicateNot.toSimpleSql(), equalTo("NOT (" + sqlLiteralNull.toSimpleSql() + ")"));
    }

    @Test
    void testGetType() {
        assertThat(sqlPredicateNot.getType(), equalTo(SqlNodeType.PREDICATE_NOT));
    }

    @Test
    void getExpression() {
        assertThat(sqlPredicateNot.getExpression(), equalTo(sqlLiteralNull));
    }
}