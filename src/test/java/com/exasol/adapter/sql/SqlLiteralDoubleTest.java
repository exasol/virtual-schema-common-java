package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void testToSimpleSql() {
        assertThat(this.sqlLiteralDouble.toSimpleSql(), equalTo("20.1"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlLiteralDouble.getType(), equalTo(SqlNodeType.LITERAL_DOUBLE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlLiteralDouble)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlLiteralDouble.accept(visitor), equalTo(sqlLiteralNull));
    }
}