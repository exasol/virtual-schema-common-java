package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlPredicateBetweenTest {
    private SqlPredicateBetween sqlPredicateBetween;
    private SqlNode expression;
    private SqlNode betweenLeft;
    private SqlNode betweenRight;

    @BeforeEach
    void setUp() {
        this.expression = new SqlLiteralDouble(12.0);
        this.betweenLeft = new SqlLiteralDouble(11.0);
        this.betweenRight = new SqlLiteralDouble(13.0);
        this.sqlPredicateBetween =
              new SqlPredicateBetween(this.expression, this.betweenLeft, this.betweenRight);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateBetween.toSimpleSql(), equalTo(
              this.expression.toSimpleSql() + " BETWEEN " + this.betweenLeft.toSimpleSql() +
                    " AND " + this.betweenRight.toSimpleSql()));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateBetween.getType(), equalTo(SqlNodeType.PREDICATE_BETWEEN));
    }

    @Test
    void getExpression() {
        assertThat(this.sqlPredicateBetween.getExpression(), equalTo(this.expression));
    }

    @Test
    void getBetweenLeft() {
        assertThat(this.sqlPredicateBetween.getBetweenLeft(), equalTo(this.betweenLeft));
    }

    @Test
    void getBetweenRight() {
        assertThat(this.sqlPredicateBetween.getBetweenRight(), equalTo(this.betweenRight));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlPredicateBetween)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlPredicateBetween.accept(visitor), equalTo(sqlLiteralNull));
    }
}