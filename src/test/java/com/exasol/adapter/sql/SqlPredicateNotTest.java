package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlLiteralNull)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlLiteralNull.accept(visitor), equalTo(sqlLiteralNull));
    }
}