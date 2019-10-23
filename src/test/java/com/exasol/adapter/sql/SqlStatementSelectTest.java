package com.exasol.adapter.sql;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlStatementSelectTest {
    @Test
    void builder() {
        final SqlStatementSelect.Builder sqlStatementSelectBuilder = SqlStatementSelect.builder();
        assertThat(sqlStatementSelectBuilder, instanceOf(SqlStatementSelect.Builder.class));
    }
}