package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
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
}
