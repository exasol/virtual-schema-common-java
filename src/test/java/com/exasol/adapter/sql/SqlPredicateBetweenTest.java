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
        expression = new SqlLiteralNull();
        betweenLeft = new SqlLiteralBool(true);
        betweenRight = new SqlLiteralBool(false);
        sqlPredicateBetween = new SqlPredicateBetween(expression, betweenLeft, betweenRight);
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlPredicateBetween.toSimpleSql(),
              equalTo(expression.toSimpleSql() + " BETWEEN "
                    + betweenLeft.toSimpleSql() + " AND "
                    + betweenRight.toSimpleSql()));
    }

    @Test
    void testGetType() {
        assertThat(sqlPredicateBetween.getType(), equalTo(SqlNodeType.PREDICATE_BETWEEN));
    }

    @Test
    void getExpression() {
        assertThat(sqlPredicateBetween.getExpression(), equalTo(expression));
    }

    @Test
    void getBetweenLeft() {
        assertThat(sqlPredicateBetween.getBetweenLeft(), equalTo(betweenLeft));
    }

    @Test
    void getBetweenRight() {
        assertThat(sqlPredicateBetween.getBetweenRight(), equalTo(betweenRight));
    }
}