package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlLiteralNullTest {
    private SqlLiteralNull sqlLiteralNull;

    @BeforeEach
    void setUp() {
        this.sqlLiteralNull = new SqlLiteralNull();
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralNull.getType(), equalTo(SqlNodeType.LITERAL_NULL));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlLiteralNull)).thenReturn(this.sqlLiteralNull);
        assertThat(this.sqlLiteralNull.accept(visitor), equalTo(this.sqlLiteralNull));
    }
}