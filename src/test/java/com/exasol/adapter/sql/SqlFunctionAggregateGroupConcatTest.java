package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqlFunctionAggregateGroupConcatTest {
    private static final boolean TEST_DISTINCT = true;
    private static final String TEST_SEPARATOR = ":";
    private static final String TEST_NAME = "test string";
    private SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat;
    private List<SqlNode> arguments;
    private AggregateFunction aggregateFunction;
    @Mock
    private SqlOrderBy sqlOrderBy;

    @BeforeEach
    void setUp() {
        this.aggregateFunction = AggregateFunction.GROUP_CONCAT;
        this.arguments = new ArrayList<>();
        this.arguments.add(new SqlLiteralString(TEST_NAME));
        this.sqlFunctionAggregateGroupConcat =
              new SqlFunctionAggregateGroupConcat(this.aggregateFunction, this.arguments, this.sqlOrderBy,
                    TEST_DISTINCT, TEST_SEPARATOR);
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getArguments(), equalTo(this.arguments));
    }

    @Test
    void testGetArgumentsReturnsEmptyList() {
        assertThat(new SqlFunctionAggregate(this.aggregateFunction, null, TEST_DISTINCT).getArguments(),
              equalTo(Collections.emptyList()));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlFunctionAggregateGroupConcat.toSimpleSql(),
              equalTo("GROUP_CONCAT(DISTINCT '" + TEST_NAME + "' null SEPARATOR ':')"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getType(), equalTo(SqlNodeType.FUNCTION_AGGREGATE));
    }

    @Test
    void testGetFunction() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getFunction(), equalTo(this.aggregateFunction));
    }

    @Test
    void getFunctionName() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getFunctionName(), equalTo("GROUP_CONCAT"));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionAggregateGroupConcat> visitor = mock(SqlNodeVisitor.class);
        when(visitor.visit(this.sqlFunctionAggregateGroupConcat)).thenReturn(this.sqlFunctionAggregateGroupConcat);
        assertThat(this.sqlFunctionAggregateGroupConcat.accept(visitor), equalTo(this.sqlFunctionAggregateGroupConcat));
    }

    @Test
    void testHasDistinct() {
        assertThat(this.sqlFunctionAggregateGroupConcat.hasDistinct(), equalTo(TEST_DISTINCT));
    }

    @Test
    void hasOrderByTrue() {
        final List<SqlNode> expressions = new ArrayList<>();
        expressions.add(new SqlLiteralString("testValue"));
        when(this.sqlOrderBy.getExpressions()).thenReturn(expressions);
        assertTrue(this.sqlFunctionAggregateGroupConcat.hasOrderBy());
    }

    @Test
    void hasOrderByFalse() {
        assertFalse(this.sqlFunctionAggregateGroupConcat.hasOrderBy());
    }

    @Test
    void getOrderBy() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getOrderBy(), equalTo(this.sqlOrderBy));
    }

    @Test
    void getSeparator() {
        assertThat(this.sqlFunctionAggregateGroupConcat.getSeparator(), equalTo(TEST_SEPARATOR));
    }
}