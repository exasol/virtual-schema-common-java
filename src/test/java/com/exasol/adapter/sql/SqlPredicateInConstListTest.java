package com.exasol.adapter.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
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
        assertThrows(UnsupportedOperationException.class,
                () -> predicate.getInArguments().add(new SqlLiteralExactnumeric(BigDecimal.valueOf(2L))));
    }

    @Test
    void testTreatsNullArgumentsAsEmptyList() {
        assertThat(new SqlPredicateInConstList(new SqlLiteralExactnumeric(BigDecimal.ZERO), null).getInArguments(),
                empty());
    }
}
