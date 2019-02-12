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

class SqlFunctionAggregateTest {
    private static final boolean TEST_DISTINCT = true;
    public static final String TEST_NAME = "test string";
    private SqlFunctionAggregate sqlFunctionAggregate;
    private List<SqlNode> arguments;
    private AggregateFunction aggregateFunction;

    @BeforeEach
    void setUp() {
        this.aggregateFunction = AggregateFunction.AVG;
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralString(TEST_NAME));
        this.sqlFunctionAggregate =
              new SqlFunctionAggregate(this.aggregateFunction, this.arguments, TEST_DISTINCT);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionAggregate.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsReturnsEmptyList() {
        assertThat(
              new SqlFunctionAggregate(this.aggregateFunction, null, TEST_DISTINCT).getArguments(),
              equalTo(Collections.emptyList()));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlFunctionAggregate.toSimpleSql(),
              equalTo("AVG(DISTINCT '" + TEST_NAME + "')"));
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
        final SqlNodeVisitor<SqlFunctionAggregate> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlFunctionAggregate)).thenReturn(this.sqlFunctionAggregate);
        assertThat(this.sqlFunctionAggregate.accept(visitor), equalTo(this.sqlFunctionAggregate));
    }

    @Test
    void testHasDistinct() {
        assertThat(this.sqlFunctionAggregate.hasDistinct(), equalTo(TEST_DISTINCT));
    }
}