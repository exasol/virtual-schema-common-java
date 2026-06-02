package com.exasol.adapter.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SqlPredicateInConstListTest {
    @Test
    void testCopiesArgumentsDefensively() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(new SqlLiteralExactnumeric(BigDecimal.ONE));
        final SqlPredicateInConstList predicate = new SqlPredicateInConstList(new SqlLiteralExactnumeric(BigDecimal.ZERO),
                arguments);
        arguments.add(new SqlLiteralExactnumeric(BigDecimal.valueOf(2L)));
        assertThat(predicate.getInArguments().size(), equalTo(1));
    }

    @Test
    void testGetArgumentsReturnsUnmodifiableList() {
        final SqlPredicateInConstList predicate = new SqlPredicateInConstList(
                new SqlLiteralExactnumeric(BigDecimal.ZERO), List.of(new SqlLiteralExactnumeric(BigDecimal.ONE)));
        final List<SqlNode> arguments = predicate.getInArguments();
        final SqlLiteralExactnumeric literal = new SqlLiteralExactnumeric(BigDecimal.valueOf(2L));
        assertThrows(UnsupportedOperationException.class, () -> arguments.add(literal));
    }

    @Test
    void testTreatsNullArgumentsAsEmptyList() {
        assertThat(new SqlPredicateInConstList(new SqlLiteralExactnumeric(BigDecimal.ZERO), null).getInArguments(),
                empty());
    }

    @Test
    void testGetChildrenPreservesArgumentOrder() {
        final SqlNode firstArgument = new SqlLiteralExactnumeric(BigDecimal.ONE);
        final SqlNode secondArgument = new SqlLiteralExactnumeric(BigDecimal.valueOf(2L));
        final SqlNode expression = new SqlLiteralExactnumeric(BigDecimal.ZERO);
        final SqlPredicateInConstList predicate = new SqlPredicateInConstList(expression,
                List.of(firstArgument, secondArgument));
        assertThat(predicate.getChildren(), contains(sameInstance(firstArgument), sameInstance(secondArgument),
                sameInstance(expression)));
    }

    @Test
    void testGetChildrenWithNullExpression() {
        final SqlNode firstArgument = new SqlLiteralExactnumeric(BigDecimal.ONE);
        final SqlNode secondArgument = new SqlLiteralExactnumeric(BigDecimal.valueOf(2L));
        final SqlPredicateInConstList predicate = new SqlPredicateInConstList(null, List.of(firstArgument, secondArgument));
        assertThat(predicate.getChildren(), contains(sameInstance(firstArgument), sameInstance(secondArgument)));
    }

    @Test
    void testGetChildrenReturnsUnmodifiableList() {
        final SqlPredicateInConstList predicate = new SqlPredicateInConstList(
                new SqlLiteralExactnumeric(BigDecimal.ZERO), List.of(new SqlLiteralExactnumeric(BigDecimal.ONE)));
        final List<SqlNode> children = predicate.getChildren();
        final SqlLiteralExactnumeric literal = new SqlLiteralExactnumeric(BigDecimal.valueOf(2L));
        assertThrows(UnsupportedOperationException.class, () -> children.add(literal));
    }
}
