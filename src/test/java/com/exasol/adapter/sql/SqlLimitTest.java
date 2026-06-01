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
    private static final int LIMIT = 5;
    private static final int OFFSET = 4;
    private SqlLimit sqlLimit;

    @BeforeEach
    void setUp() {
        this.sqlLimit = new SqlLimit(LIMIT);
    }

    @Test
    void testCreateAssertWithNegativeLimitThrowsException() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.sqlLimit = new SqlLimit(-1));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-27"));
    }

    @Test
    void testCreateAssertWithNegativeOffsetThrowsException() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.sqlLimit = new SqlLimit(1, -5));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-27"));
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
    void getLimit() {
        assertThat(this.sqlLimit.getLimit(), equalTo(LIMIT));
    }

    @Test
    @SuppressWarnings("removal")
    void setLimitThrowsUnsupportedOperationException() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> this.sqlLimit.setLimit(10));
        assertThat(exception.getMessage(), equalTo("E-VSCOMJAVA-42: SqlLimit is immutable. Create a new instance instead."));
    }

    @Test
    void hasOffsetReturnsFalseForDefaultOffset() {
        assertFalse(this.sqlLimit.hasOffset());
    }

    @Test
    @SuppressWarnings("removal")
    void setOffsetThrowsUnsupportedOperationException() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> this.sqlLimit.setOffset(OFFSET));
        assertThat(exception.getMessage(), equalTo("E-VSCOMJAVA-43: SqlLimit is immutable. Create a new instance instead."));
    }

    @Test
    void createsWithExplicitOffset() {
        this.sqlLimit = new SqlLimit(LIMIT, OFFSET);
        assertAll(() -> assertTrue(this.sqlLimit.hasOffset()),
                () -> assertThat(this.sqlLimit.getOffset(), equalTo(OFFSET)));
    }

    @Test
    void allowsZeroValues() {
        this.sqlLimit = new SqlLimit(0, 0);
        assertAll(() -> assertThat(this.sqlLimit.getLimit(), equalTo(0)),
                () -> assertThat(this.sqlLimit.getOffset(), equalTo(0)),
                () -> assertFalse(this.sqlLimit.hasOffset()));
    }
}
