package com.exasol.adapter.sql;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SqlStatementSelectTest {
    @Test
    void builder() {
        final SqlStatementSelect.Builder sqlStatementSelectBuilder = SqlStatementSelect.builder();
        assertThat(sqlStatementSelectBuilder, instanceOf(SqlStatementSelect.Builder.class));
    }

    @Test
    void buildWithoutRequiredClausesDoesNotThrow() {
        final SqlStatementSelect select = assertDoesNotThrow(() -> SqlStatementSelect.builder().build());

        assertThat(select.getFromClause(), nullValue());
        assertThat(select.getSelectList(), nullValue());
    }

    @Test
    void buildWithOnlyFromClauseDoesNotThrow() {
        final SqlTable fromClause = new SqlTable("MY_TABLE", null);

        final SqlStatementSelect select = assertDoesNotThrow(
                () -> SqlStatementSelect.builder().fromClause(fromClause).build());

        assertThat(select.getFromClause(), sameInstance(fromClause));
        assertThat(select.getSelectList(), nullValue());
    }

    @Test
    void buildWithOnlySelectListDoesNotThrow() {
        final SqlSelectList selectList = SqlSelectList.createAnyValueSelectList();

        final SqlStatementSelect select = assertDoesNotThrow(
                () -> SqlStatementSelect.builder().selectList(selectList).build());

        assertThat(select.getFromClause(), nullValue());
        assertThat(select.getSelectList(), sameInstance(selectList));
    }

    @Test
    void buildSetsParentForPresentRequiredClauses() {
        final SqlTable fromClause = new SqlTable("MY_TABLE", null);
        final SqlSelectList selectList = SqlSelectList.createAnyValueSelectList();

        final SqlStatementSelect select = SqlStatementSelect.builder().fromClause(fromClause).selectList(selectList)
                .build();

        assertThat(fromClause.getParent(), sameInstance(select));
        assertThat(selectList.getParent(), sameInstance(select));
    }

    @Test
    void getChildrenReturnsEmptyListForEmptyBuilder() {
        final SqlStatementSelect select = SqlStatementSelect.builder().build();

        assertThat(select.getChildren(), empty());
    }

    @Test
    void getChildrenReturnsChildrenInClauseOrder() {
        final SqlTable fromClause = new SqlTable("MY_TABLE", null);
        final SqlLiteralBool projectedColumn = new SqlLiteralBool(true);
        final SqlSelectList selectList = SqlSelectList.createRegularSelectList(List.of(projectedColumn));
        final SqlLiteralBool whereClause = new SqlLiteralBool(false);
        final SqlLiteralBool groupByExpression = new SqlLiteralBool(true);
        final SqlGroupBy groupBy = new SqlGroupBy(List.of(groupByExpression));
        final SqlLiteralBool having = new SqlLiteralBool(true);
        final SqlLiteralBool orderByExpression = new SqlLiteralBool(false);
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(orderByExpression), List.of(true), List.of(true));
        final SqlLimit limit = new SqlLimit(10);
        final SqlStatementSelect select = SqlStatementSelect.builder().fromClause(fromClause).selectList(selectList)
                .whereClause(whereClause).groupBy(groupBy).having(having).orderBy(orderBy).limit(limit).build();

        assertThat(select.getChildren(), contains(fromClause, projectedColumn, whereClause, groupByExpression, having,
                orderByExpression, limit));
    }
}
