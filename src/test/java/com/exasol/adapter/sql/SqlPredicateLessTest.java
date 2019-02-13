package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateLessTest {
    private SqlPredicateLess sqlPredicateLess;
    private SqlLiteralDouble sqlLiteralDoubleLeft;
    private SqlLiteralDouble sqlLiteralDoubleRight;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralDouble(20.5);
        this.sqlLiteralDoubleRight = new SqlLiteralDouble(21.5);
        this.sqlPredicateLess =
              new SqlPredicateLess(this.sqlLiteralDoubleLeft, this.sqlLiteralDoubleRight);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateLess.toSimpleSql(), equalTo("20.5 < 21.5"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateLess.getType(), equalTo(SqlNodeType.PREDICATE_LESS));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateLess> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlPredicateLess)).thenReturn(this.sqlPredicateLess);
        assertThat(this.sqlPredicateLess.accept(visitor), equalTo(this.sqlPredicateLess));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateLess.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void testGetRight() {
        assertThat(this.sqlPredicateLess.getRight(), equalTo(this.sqlLiteralDoubleRight));
    }
}