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
    private final static boolean IS_INFIX = true;
    private final static boolean IS_PREFIX = false;
    private SqlFunctionScalar sqlFunctionScalar;
    private ScalarFunction scalarFunction;
    private List<SqlNode> arguments;

    @BeforeEach
    void setUp() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralDouble(2));
        this.arguments.add(new SqlLiteralDouble(5));
        this.scalarFunction = ScalarFunction.ADD;
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, this.arguments, IS_INFIX, IS_PREFIX);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalar.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsEmptyList() {
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, null, IS_INFIX, IS_PREFIX);
        assertThat(this.sqlFunctionScalar.getArguments(), equalTo(Collections.emptyList()));
    }

    @Test
    void testToSimpleSqlInfix() {
        assertThat(this.sqlFunctionScalar.toSimpleSql(), equalTo("(2.0 + 5.0)"));
    }

    @Test
    void testToSimpleSqlPrefix() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralDouble(2));
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, this.arguments, false, true);
        assertThat(this.sqlFunctionScalar.toSimpleSql(), equalTo("(ADD 2.0)"));
    }

    @Test
    void testToSimpleSql() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralDouble(2));
        this.sqlFunctionScalar = new SqlFunctionScalar(this.scalarFunction, this.arguments, false, false);
        assertThat(this.sqlFunctionScalar.toSimpleSql(), equalTo("ADD(2.0)"));
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

    @Test
    void getNumArgs() {
        assertThat(this.sqlFunctionScalar.getNumArgs(), equalTo(2));
    }

    @Test
    void isInfix() {
        assertThat(this.sqlFunctionScalar.isInfix(), equalTo(IS_INFIX));
    }

    @Test
    void isPrefix() {
        assertThat(this.sqlFunctionScalar.isPrefix(), equalTo(IS_PREFIX));
    }
}