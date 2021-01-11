package com.exasol.adapter.sql;

import static com.exasol.mocking.MockUtils.mockSqlNodeVisitor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;

class SqlLimitTest {
    private static final Integer LIMIT = 5;
    private static final Integer OFFSET = 4;
    private SqlLimit sqlLimit;

    @BeforeEach
    void setUp() {
        this.sqlLimit = new SqlLimit(LIMIT);
    }

    @Test
    void testCreateAssertWithNegativeLimitThrowsException() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.sqlLimit = new SqlLimit(-1));
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-27"));
    }

    @Test
    void testCreateAssertWithNegativeOffsetThrowsException() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.sqlLimit = new SqlLimit(1, -5));
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-27"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLimit.getType(), equalTo(SqlNodeType.LIMIT));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLimit> visitor = mockSqlNodeVisitor();
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
        this.sqlLimit.setOffset(OFFSET);
        assertAll(() -> assertTrue(this.sqlLimit.hasOffset()),
                () -> assertThat(this.sqlLimit.getOffset(), equalTo(OFFSET)));
    }
}