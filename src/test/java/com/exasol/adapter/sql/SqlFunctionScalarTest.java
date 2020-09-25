package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlFunctionScalarTest {
    private SqlFunctionScalar sqlFunctionScalar;
    private ScalarFunction scalarFunction;
    private List<SqlNode> arguments;

    @BeforeEach
    void setUp() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralDouble(2));
        this.arguments.add(new SqlLiteralDouble(5));
        this.scalarFunction = ScalarFunction.ADD;
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, this.arguments);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalar.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsEmptyList() {
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, null);
        assertThat(this.sqlFunctionScalar.getArguments(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalar.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionScalar.getFunction(), equalTo(ScalarFunction.ADD));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalar> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionScalar)).thenReturn(this.sqlFunctionScalar);
        assertThat(this.sqlFunctionScalar.accept(visitor), equalTo(this.sqlFunctionScalar));
    }
}