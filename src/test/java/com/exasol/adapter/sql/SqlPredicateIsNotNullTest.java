package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateIsNotNullTest {
    private SqlPredicateIsNotNull sqlPredicateIsNotNull;
    private SqlLiteralString sqlLiteralString;

    @BeforeEach
    void setUp() {
        this.sqlLiteralString = new SqlLiteralString("test_string");
        this.sqlPredicateIsNotNull = new SqlPredicateIsNotNull(this.sqlLiteralString);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateIsNotNull.toSimpleSql(),
              equalTo(this.sqlLiteralString.toSimpleSql() + " IS NOT NULL"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateIsNotNull.getType(),
              equalTo(SqlNodeType.PREDICATE_IS_NOT_NULL));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateIsNotNull.getExpression(), equalTo(this.sqlLiteralString));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlPredicateIsNotNull)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlPredicateIsNotNull.accept(visitor), equalTo(sqlLiteralNull));
    }
}