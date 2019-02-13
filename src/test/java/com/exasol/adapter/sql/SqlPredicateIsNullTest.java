package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateIsNullTest {
    private SqlPredicateIsNull sqlPredicateIsNull;
    private SqlLiteralString sqlLiteralString;

    @BeforeEach
    void setUp() {
        this.sqlLiteralString = new SqlLiteralString("");
        this.sqlPredicateIsNull = new SqlPredicateIsNull(this.sqlLiteralString);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateIsNull.toSimpleSql(),
              equalTo(this.sqlLiteralString.toSimpleSql() + " IS NULL"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateIsNull.getType(), equalTo(SqlNodeType.PREDICATE_IS_NULL));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateIsNull.getExpression(), equalTo(this.sqlLiteralString));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateIsNull> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlPredicateIsNull)).thenReturn(this.sqlPredicateIsNull);
        assertThat(this.sqlPredicateIsNull.accept(visitor), equalTo(this.sqlPredicateIsNull));
    }
}