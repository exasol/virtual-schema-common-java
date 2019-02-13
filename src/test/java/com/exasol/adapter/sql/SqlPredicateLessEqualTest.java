package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateLessEqualTest {
    private SqlPredicateLessEqual sqlPredicateLessEqual;
    private SqlLiteralDouble sqlLiteralDoubleLeft;
    private SqlLiteralDouble sqlLiteralDoubleRight;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralDouble(20.5);
        this.sqlLiteralDoubleRight = new SqlLiteralDouble(21.5);
        this.sqlPredicateLessEqual =
              new SqlPredicateLessEqual(this.sqlLiteralDoubleLeft, this.sqlLiteralDoubleRight);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateLessEqual.toSimpleSql(), equalTo("20.5 <= 21.5"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateLessEqual.getType(), equalTo(SqlNodeType.PREDICATE_LESSEQUAL));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateLessEqual> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlPredicateLessEqual)).thenReturn(this.sqlPredicateLessEqual);
        assertThat(this.sqlPredicateLessEqual.accept(visitor), equalTo(this.sqlPredicateLessEqual));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateLessEqual.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void testGetRight() {
        assertThat(this.sqlPredicateLessEqual.getRight(), equalTo(this.sqlLiteralDoubleRight));
    }

}