package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlFunctionScalarExtractTest {
    private SqlFunctionScalarExtract sqlFunctionScalarExtract;
    private List<SqlNode> arguments;

    @BeforeEach
    void setUp() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralTimestamp("2019-02-12 12:07:00"));
        this.sqlFunctionScalarExtract = new SqlFunctionScalarExtract("SECOND", this.arguments);
    }

    @Test
    void getToExtract() {
        assertThat(this.sqlFunctionScalarExtract.getToExtract(), equalTo("SECOND"));
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarExtract.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsWithNullAsArgumentList() {
        assertThrows(IllegalArgumentException.class,
                () -> this.sqlFunctionScalarExtract = new SqlFunctionScalarExtract("SECOND", null));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarExtract.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_EXTRACT));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarExtract> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionScalarExtract)).thenReturn(this.sqlFunctionScalarExtract);
        assertThat(this.sqlFunctionScalarExtract.accept(visitor), equalTo(this.sqlFunctionScalarExtract));
    }
}