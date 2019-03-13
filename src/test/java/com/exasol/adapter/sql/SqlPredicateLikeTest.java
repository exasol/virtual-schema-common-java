package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateLikeTest {
    private SqlPredicateLike sqlPredicateLike;
    private SqlLiteralString sqlLiteralDoubleLeft;
    private SqlLiteralString pattern;
    private SqlLiteralString escapeChar;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralString("abcd");
        this.pattern = new SqlLiteralString("a_d");
        this.escapeChar = new SqlLiteralString("\\%%t");
        this.sqlPredicateLike = new SqlPredicateLike(this.sqlLiteralDoubleLeft, this.pattern, this.escapeChar);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateLike.toSimpleSql(), equalTo("'abcd' LIKE 'a_d' ESCAPE '\\%%t'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateLike.getType(), equalTo(SqlNodeType.PREDICATE_LIKE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateLike> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlPredicateLike)).thenReturn(this.sqlPredicateLike);
        assertThat(this.sqlPredicateLike.accept(visitor), equalTo(this.sqlPredicateLike));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateLike.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void getPattern() {
        assertThat(this.sqlPredicateLike.getPattern(), equalTo(this.pattern));
    }

    @Test
    void getEscapeChar() {
        assertThat(this.sqlPredicateLike.getEscapeChar(), equalTo(this.escapeChar));
    }
}