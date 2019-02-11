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
        this.sqlLiteralNull = new SqlLiteralNull();
        this.sqlPredicateNot = new SqlPredicateNot(this.sqlLiteralNull);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateNot.toSimpleSql(),
              equalTo("NOT (" + this.sqlLiteralNull.toSimpleSql() + ")"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateNot.getType(), equalTo(SqlNodeType.PREDICATE_NOT));
    }

    @Test
    void getExpression() {
        assertThat(this.sqlPredicateNot.getExpression(), equalTo(this.sqlLiteralNull));
    }
}