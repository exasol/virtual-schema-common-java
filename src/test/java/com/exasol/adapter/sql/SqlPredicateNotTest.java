package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateNotTest {
    private SqlPredicateNot sqlPredicateNot;
    private SqlLiteralNull sqlLiteralNull;

    @BeforeEach
    void setUp() {
        this.sqlLiteralNull = new SqlLiteralNull();
        this.sqlPredicateNot = new SqlPredicateNot(this.sqlLiteralNull);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateNot.getType(), equalTo(SqlNodeType.PREDICATE_NOT));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateNot.getExpression(), equalTo(this.sqlLiteralNull));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateNot> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateNot)).thenReturn(this.sqlPredicateNot);
        assertThat(this.sqlPredicateNot.accept(visitor), equalTo(this.sqlPredicateNot));
    }
}