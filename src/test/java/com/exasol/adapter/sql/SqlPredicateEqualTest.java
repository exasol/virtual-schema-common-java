package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateEqualTest {
    private SqlPredicateEqual sqlPredicateEqual;
    private SqlLiteralDouble sqlLiteralDoubleLeft;
    private SqlLiteralDouble sqlLiteralDoubleRight;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDoubleLeft = new SqlLiteralDouble(20.5);
        this.sqlLiteralDoubleRight = new SqlLiteralDouble(20.5);
        this.sqlPredicateEqual =
              new SqlPredicateEqual(this.sqlLiteralDoubleLeft, this.sqlLiteralDoubleRight);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateEqual.toSimpleSql(), equalTo("20.5 = 20.5"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateEqual.getType(), equalTo(SqlNodeType.PREDICATE_EQUAL));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateEqual> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlPredicateEqual)).thenReturn(this.sqlPredicateEqual);
        assertThat(this.sqlPredicateEqual.accept(visitor), equalTo(this.sqlPredicateEqual));
    }

    @Test
    void testGetLeft() {
        assertThat(this.sqlPredicateEqual.getLeft(), equalTo(this.sqlLiteralDoubleLeft));
    }

    @Test
    void testGetRight() {
        assertThat(this.sqlPredicateEqual.getRight(), equalTo(this.sqlLiteralDoubleRight));
    }
}