package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlLiteralTimestampTest {
    private static final String VALUE = "2019-02-07 23:59:00";
    private SqlLiteralTimestamp sqlLiteralTimestamp;

    @BeforeEach
    void setUp() {
        this.sqlLiteralTimestamp = new SqlLiteralTimestamp(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralTimestamp.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralTimestamp.toSimpleSql(), equalTo("TIMESTAMP '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralTimestamp.getType(), equalTo(SqlNodeType.LITERAL_TIMESTAMP));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlLiteralTimestamp)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlLiteralTimestamp.accept(visitor), equalTo(sqlLiteralNull));
    }
}