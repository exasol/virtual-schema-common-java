package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlLimitTest {
    private static final Integer LIMIT = 5;
    private static final Integer OFFSET = 4;
    private SqlLimit sqlLimit;

    @BeforeEach
    void setUp() {
        this.sqlLimit = new SqlLimit(LIMIT);
    }

    @Test
    void testToSimpleSql() {
        this.sqlLimit.setOffset(10);
        assertThat(this.sqlLimit.toSimpleSql(), equalTo("LIMIT 5 OFFSET 10"));
    }

    @Test
    void testCreateAssertWithNegativeLimitThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> this.sqlLimit = new SqlLimit(-1));
    }

    @Test
    void testCreateAssertWithNegativeOffsetThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> this.sqlLimit = new SqlLimit(1, -5));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLimit.getType(), equalTo(SqlNodeType.LIMIT));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLimit> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlLimit)).thenReturn(this.sqlLimit);
        assertThat(this.sqlLimit.accept(visitor), equalTo(this.sqlLimit));
    }

    @Test
    void getOffset() {
        assertThat(this.sqlLimit.getOffset(), equalTo(0));
    }

    @Test
    void setLimit() {
        assertThat(this.sqlLimit.getLimit(), equalTo(LIMIT));
        this.sqlLimit.setLimit(10);
        assertThat(this.sqlLimit.getLimit(), equalTo(10));
    }

    @Test
    void setOffset() {
        assertFalse(this.sqlLimit.hasOffset());
        this.sqlLimit.setOffset(3);
        assertTrue(this.sqlLimit.hasOffset());
        assertThat(this.sqlLimit.getOffset(), equalTo(3));
    }
}