package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlPredicateBetweenTest {
    private SqlPredicateBetween sqlPredicateBetween;
    private SqlNode expression;
    private SqlNode betweenLeft;
    private SqlNode betweenRight;

    @BeforeEach
    void setUp() {
        this.expression = new SqlLiteralNull();
        this.betweenLeft = new SqlLiteralBool(true);
        this.betweenRight = new SqlLiteralBool(false);
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
}