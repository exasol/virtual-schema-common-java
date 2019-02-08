package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlFunctionScalarCaseTest {
    private SqlFunctionScalarCase sqlFunctionScalarCase;
    private List<SqlNode> arguments;
    private List<SqlNode> results;
    private SqlLiteralNull basis;

    @BeforeEach
    void setUp() {
        arguments = new ArrayList<>();
        arguments.add(new SqlLiteralNull());
        results = new ArrayList<>();
        results.add(new SqlLiteralNull());
        basis = new SqlLiteralNull();
        sqlFunctionScalarCase = new SqlFunctionScalarCase(arguments, results, basis);
    }

    @Test
    void testGetArguments() {
        assertThat(sqlFunctionScalarCase.getArguments(), equalTo(arguments));
        sqlFunctionScalarCase = new SqlFunctionScalarCase(null, results, new SqlLiteralNull());
        assertThat(sqlFunctionScalarCase.getArguments(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetResults() {
        assertThat(sqlFunctionScalarCase.getResults(), equalTo(results));
        sqlFunctionScalarCase = new SqlFunctionScalarCase(null, null, new SqlLiteralNull());
        assertThat(sqlFunctionScalarCase.getResults(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetBasis() {
        assertThat(sqlFunctionScalarCase.getBasis(), equalTo(basis));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlFunctionScalarCase.toSimpleSql(), equalTo("CASE"));
    }

    @Test
    void testGetType() {
        assertThat(sqlFunctionScalarCase.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_CASE));
    }

    @Test
    void testGetFunction() {
        assertThat(sqlFunctionScalarCase.getFunction(), equalTo(ScalarFunction.CASE));
    }
}