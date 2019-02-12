package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlLiteralStringTest {
    private static final String VALUE = "test string";
    private SqlLiteralString sqlLiteralString;

    @BeforeEach
    void setUp() {
        this.sqlLiteralString = new SqlLiteralString(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralString.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralString.toSimpleSql(), equalTo("'" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralString.getType(), equalTo(SqlNodeType.LITERAL_STRING));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralString> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlLiteralString)).thenReturn(this.sqlLiteralString);
        assertThat(this.sqlLiteralString.accept(visitor), equalTo(this.sqlLiteralString));
    }
}