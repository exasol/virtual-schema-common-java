package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlLiteralDateTest {
    private static final String VALUE = "2019-02-07";
    private SqlLiteralDate sqlLiteralDate;

    @BeforeEach
    void setUp() {
        this.sqlLiteralDate = new SqlLiteralDate(VALUE);
    }

    @Test
    void testGetValue() {
        assertThat(this.sqlLiteralDate.getValue(), equalTo(VALUE));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlLiteralDate.toSimpleSql(), equalTo("DATE '" + VALUE + "'"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralDate.getType(), equalTo(SqlNodeType.LITERAL_DATE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralDate> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlLiteralDate)).thenReturn(this.sqlLiteralDate);
        assertThat(this.sqlLiteralDate.accept(visitor), equalTo(this.sqlLiteralDate));
    }
}