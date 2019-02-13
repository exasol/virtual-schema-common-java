package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlFunctionScalarCastTest {
    private SqlFunctionScalarCast sqlFunctionScalarCast;
    private List<SqlNode> arguments;
    private DataType dataType;
    private SqlNode sqlNode;

    @BeforeEach
    void setUp() {
        this.dataType = DataType.createDecimal(18, 0);
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralDouble(20));
        this.sqlFunctionScalarCast = new SqlFunctionScalarCast(this.dataType, this.arguments);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarCast.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsWithNullAsArgumentList() {
        assertThrows(IllegalArgumentException.class,
              () -> this.sqlFunctionScalarCast = new SqlFunctionScalarCast(this.dataType, null));
    }

    @Test
    void testGetDataType() {
        assertThat(this.sqlFunctionScalarCast.getDataType(), equalTo(this.dataType));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlFunctionScalarCast.toSimpleSql(), equalTo("CAST (20.0 AS DECIMAL(18, 0))"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarCast.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_CAST));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionScalarCast.getFunction(), equalTo(ScalarFunction.CAST));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarCast> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlFunctionScalarCast)).thenReturn(this.sqlFunctionScalarCast);
        assertThat(this.sqlFunctionScalarCast.accept(visitor), equalTo(this.sqlFunctionScalarCast));
    }
}