package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateIsNotNullTest {
    private SqlPredicateIsNotNull sqlPredicateIsNotNull;
    private SqlLiteralString sqlLiteralString;

    @BeforeEach
    void setUp() {
        this.sqlLiteralString = new SqlLiteralString("test_string");
        this.sqlPredicateIsNotNull = new SqlPredicateIsNotNull(this.sqlLiteralString);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateIsNotNull.getType(), equalTo(SqlNodeType.PREDICATE_IS_NOT_NULL));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateIsNotNull.getExpression(), equalTo(this.sqlLiteralString));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateIsNotNull> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateIsNotNull)).thenReturn(this.sqlPredicateIsNotNull);
        assertThat(this.sqlPredicateIsNotNull.accept(visitor), equalTo(this.sqlPredicateIsNotNull));
    }
}