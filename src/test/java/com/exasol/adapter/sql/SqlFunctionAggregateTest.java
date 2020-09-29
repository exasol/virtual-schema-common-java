package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlFunctionAggregateTest {
    private static final boolean TEST_DISTINCT = true;
    private static final String TEST_NAME = "test string";
    private SqlFunctionAggregate sqlFunctionAggregate;
    private List<SqlNode> arguments;
    private AggregateFunction aggregateFunction;

    @BeforeEach
    void setUp() {
        this.aggregateFunction = AggregateFunction.AVG;
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralString(TEST_NAME));
        this.sqlFunctionAggregate = new SqlFunctionAggregate(this.aggregateFunction, this.arguments, TEST_DISTINCT);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionAggregate.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsReturnsEmptyList() {
        assertThat(new SqlFunctionAggregate(this.aggregateFunction, null, TEST_DISTINCT).getArguments(),
                equalTo(Collections.emptyList()));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionAggregate.getType(), equalTo(SqlNodeType.FUNCTION_AGGREGATE));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionAggregate.getFunction(), equalTo(this.aggregateFunction));
    }

    @Test
    void getFunctionName() {
        assertThat(this.sqlFunctionAggregate.getFunctionName(), equalTo("AVG"));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionAggregate> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionAggregate)).thenReturn(this.sqlFunctionAggregate);
        assertThat(this.sqlFunctionAggregate.accept(visitor), equalTo(this.sqlFunctionAggregate));
    }

    @Test
    void testHasDistinct() {
        assertThat(this.sqlFunctionAggregate.hasDistinct(), equalTo(TEST_DISTINCT));
    }
}