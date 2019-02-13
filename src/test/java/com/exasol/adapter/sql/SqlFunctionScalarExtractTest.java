package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlFunctionScalarExtractTest {
    private static final String TEST_STRING_TO_EXTRACT = "SECOND";
    private SqlFunctionScalarExtract sqlFunctionScalarExtract;
    private List<SqlNode> arguments;

    @BeforeEach
    void setUp() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralTimestamp("2019-02-12 12:07:00"));
        this.sqlFunctionScalarExtract =
              new SqlFunctionScalarExtract(TEST_STRING_TO_EXTRACT, this.arguments);
    }

    @Test
    void getToExtract() {
        assertThat(this.sqlFunctionScalarExtract.getToExtract(), equalTo(TEST_STRING_TO_EXTRACT));
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarExtract.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsWithNullAsArgumentList() {
        assertThrows(IllegalArgumentException.class, () -> this.sqlFunctionScalarExtract =
              new SqlFunctionScalarExtract(this.TEST_STRING_TO_EXTRACT, null));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlFunctionScalarExtract.toSimpleSql(), equalTo(
              "EXTRACT (" + TEST_STRING_TO_EXTRACT + " FROM TIMESTAMP '2019-02-12 12:07:00')"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarExtract.getType(),
              equalTo(SqlNodeType.FUNCTION_SCALAR_EXTRACT));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionScalarExtract.getFunction(), equalTo(ScalarFunction.EXTRACT));
    }

    @Test
    void getFunctionName() {
        assertThat(this.sqlFunctionScalarExtract.getFunctionName(), equalTo("EXTRACT"));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarExtract> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlFunctionScalarExtract))
              .thenReturn(this.sqlFunctionScalarExtract);
        assertThat(this.sqlFunctionScalarExtract.accept(visitor),
              equalTo(this.sqlFunctionScalarExtract));
    }
}
