package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateNotEqualTest {
    private SqlPredicateNotEqual sqlPredicateNotEqual;
    private SqlLiteralDouble sqlLiteralDoubleLeft;
    private SqlLiteralDouble sqlLiteralDoubleRight;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralDouble(21.5);
        this.sqlLiteralDoubleRight = new SqlLiteralDouble(20.5);
        this.sqlPredicateNotEqual = new SqlPredicateNotEqual(this.sqlLiteralDoubleLeft, this.sqlLiteralDoubleRight);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateNotEqual.getType(), equalTo(SqlNodeType.PREDICATE_NOTEQUAL));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateNotEqual> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateNotEqual)).thenReturn(this.sqlPredicateNotEqual);
        assertThat(this.sqlPredicateNotEqual.accept(visitor), equalTo(this.sqlPredicateNotEqual));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateNotEqual.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void testGetRight() {
        assertThat(this.sqlPredicateNotEqual.getRight(), equalTo(this.sqlLiteralDoubleRight));
    }
}