package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlLiteralDoubleTest {
    private static final Double VALUE = 20.1;
    private SqlLiteralDouble sqlLiteralDouble;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDouble = new SqlLiteralDouble(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralDouble.getValue(), equalTo(VALUE));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralDouble.getType(), equalTo(SqlNodeType.LITERAL_DOUBLE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralDouble> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlLiteralDouble)).thenReturn(this.sqlLiteralDouble);
        assertThat(this.sqlLiteralDouble.accept(visitor), equalTo(this.sqlLiteralDouble));
    }
}