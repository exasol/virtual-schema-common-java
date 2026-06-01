package com.exasol.adapter.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SqlOrderByTest {
    @Test
    void testCopiesListsDefensively() {
        final List<SqlNode> expressions = new ArrayList<>();
        expressions.add(new SqlLiteralBool(true));
        final List<Boolean> isAsc = new ArrayList<>(List.of(true));
        final List<Boolean> nullsLast = new ArrayList<>(List.of(false));
        final SqlOrderBy orderBy = new SqlOrderBy(expressions, isAsc, nullsLast);
        expressions.add(new SqlLiteralBool(false));
        isAsc.add(false);
        nullsLast.add(true);
        assertThat(orderBy.getExpressions().size(), equalTo(1));
        assertThat(orderBy.isAscending().size(), equalTo(1));
        assertThat(orderBy.nullsLast().size(), equalTo(1));
    }

    @Test
    void testGettersReturnUnmodifiableLists() {
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(new SqlLiteralBool(true)), List.of(true), List.of(false));
        final List<SqlNode> expressions = orderBy.getExpressions();
        final List<Boolean> isAscending = orderBy.isAscending();
        final List<Boolean> nullsLast = orderBy.nullsLast();
        final SqlLiteralBool literal = new SqlLiteralBool(false);
        assertThrows(UnsupportedOperationException.class, () -> expressions.add(literal));
        assertThrows(UnsupportedOperationException.class, () -> isAscending.add(false));
        assertThrows(UnsupportedOperationException.class, () -> nullsLast.add(true));
    }

    @Test
    void testTreatsNullListsAsEmpty() {
        final SqlOrderBy orderBy = new SqlOrderBy(null, null, null);
        assertThat(orderBy.getExpressions(), empty());
        assertThat(orderBy.isAscending(), empty());
        assertThat(orderBy.nullsLast(), empty());
    }

    @Test
    void testValidatesParallelListSizesInConstructor() {
        final List<SqlNode> expressions = List.of(new SqlLiteralBool(true));
        final List<Boolean> isAsc = List.of(true, false);
        final List<Boolean> nullsFirst = List.of(false);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new SqlOrderBy(expressions, isAsc, nullsFirst));
        assertThat(exception.getMessage(), equalTo(
                "F-VSCOMJAVA-46: Can not create SqlOrderBy with an invalid format. The size of the three lists must be equal. "
                        + "This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    @Test
    void testSetsParentForCopiedExpressionNodes() {
        final SqlLiteralBool expression = new SqlLiteralBool(true);
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(expression), List.of(true), List.of(false));
        assertThat(expression.getParent(), sameInstance(orderBy));
    }
}
