package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateLikeRegexpTest {
    private SqlPredicateLikeRegexp sqlPredicateLikeRegexp;
    private SqlLiteralString sqlLiteralDoubleLeft;
    private SqlLiteralString pattern;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralString("abcd");
        this.pattern = new SqlLiteralString("a_d");
        this.sqlPredicateLikeRegexp = new SqlPredicateLikeRegexp(this.sqlLiteralDoubleLeft, this.pattern);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateLikeRegexp.getType(), equalTo(SqlNodeType.PREDICATE_LIKE_REGEXP));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateLikeRegexp> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateLikeRegexp)).thenReturn(this.sqlPredicateLikeRegexp);
        assertThat(this.sqlPredicateLikeRegexp.accept(visitor), equalTo(this.sqlPredicateLikeRegexp));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateLikeRegexp.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void getPattern() {
        assertThat(this.sqlPredicateLikeRegexp.getPattern(), equalTo(this.pattern));
    }
}