package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import com.exasol.mocking.MockUtils;

class SqlFunctionScalarCastTest {
    private SqlFunctionScalarCast sqlFunctionScalarCast;
    private SqlNode argument;
    private DataType dataType;

    @BeforeEach
    void setUp() {
        this.dataType = DataType.createDecimal(18, 0);
        this.argument = new SqlLiteralDouble(20);
        this.sqlFunctionScalarCast = new SqlFunctionScalarCast(this.dataType, this.argument);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarCast.getArgument(), equalTo(this.argument));
    }

    @Test
    void testGetDataType() {
        assertThat(this.sqlFunctionScalarCast.getDataType(), equalTo(this.dataType));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarCast.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_CAST));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarCast> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionScalarCast)).thenReturn(this.sqlFunctionScalarCast);
        assertThat(this.sqlFunctionScalarCast.accept(visitor), equalTo(this.sqlFunctionScalarCast));
    }
}