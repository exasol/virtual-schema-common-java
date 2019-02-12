package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlFunctionScalarCaseTest {
    private SqlFunctionScalarCase sqlFunctionScalarCase;
    private List<SqlNode> arguments;
    private List<SqlNode> results;
    private SqlLiteralNull basis;

    @BeforeEach
    void setUp() {
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralNull());
        this.results = new ArrayList<>();
        this.results.add(new SqlLiteralNull());
        this.basis = new SqlLiteralNull();
        this.sqlFunctionScalarCase =
              new SqlFunctionScalarCase(this.arguments, this.results, this.basis);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarCase.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsEmptyList() {
        this.sqlFunctionScalarCase =
              new SqlFunctionScalarCase(null, this.results, new SqlLiteralNull());
        assertThat(this.sqlFunctionScalarCase.getArguments(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetResults() {
        assertThat(this.sqlFunctionScalarCase.getResults(), equalTo(this.results));
    }

    @Test
    void testGetResultsEmptyList() {
        this.sqlFunctionScalarCase = new SqlFunctionScalarCase(null, null, new SqlLiteralNull());
        assertThat(this.sqlFunctionScalarCase.getResults(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetBasis() {
        assertThat(this.sqlFunctionScalarCase.getBasis(), equalTo(this.basis));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlFunctionScalarCase.toSimpleSql(), equalTo("CASE"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarCase.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_CASE));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionScalarCase.getFunction(), equalTo(ScalarFunction.CASE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlFunctionScalarCase)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlFunctionScalarCase.accept(visitor), equalTo(sqlLiteralNull));
    }
}