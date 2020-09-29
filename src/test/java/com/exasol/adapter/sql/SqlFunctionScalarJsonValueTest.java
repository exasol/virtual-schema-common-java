package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import com.exasol.mocking.MockUtils;

@ExtendWith(MockitoExtension.class)
class SqlFunctionScalarJsonValueTest {
    private SqlFunctionScalarJsonValue sqlFunctionScalarJsonValue;
    private SqlFunctionScalarJsonValue.Behavior emptyBehavior;
    private SqlFunctionScalarJsonValue.Behavior errorBehavior;

    @BeforeEach
    void setUp() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(new SqlLiteralString("{\"a\": 1}"));
        arguments.add(new SqlLiteralString("$.a"));
        this.emptyBehavior = new SqlFunctionScalarJsonValue.Behavior(SqlFunctionScalarJsonValue.BehaviorType.ERROR,
                Optional.empty());
        this.errorBehavior = new SqlFunctionScalarJsonValue.Behavior(SqlFunctionScalarJsonValue.BehaviorType.DEFAULT,
                Optional.of(new SqlLiteralString("*** error ***")));
        this.sqlFunctionScalarJsonValue = new SqlFunctionScalarJsonValue(ScalarFunction.JSON_VALUE, arguments,
                DataType.createVarChar(1000, DataType.ExaCharset.UTF8), this.emptyBehavior, this.errorBehavior);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarJsonValue.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_JSON_VALUE));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarJsonValue> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionScalarJsonValue)).thenReturn(this.sqlFunctionScalarJsonValue);
        assertThat(this.sqlFunctionScalarJsonValue.accept(visitor), equalTo(this.sqlFunctionScalarJsonValue));
    }

    @Test
    void testGetScalarFunction() {
        assertThat(this.sqlFunctionScalarJsonValue.getScalarFunction(), equalTo(ScalarFunction.JSON_VALUE));
    }

    @Test
    void testGetReturningDataType() {
        assertThat(this.sqlFunctionScalarJsonValue.getReturningDataType(),
                equalTo(DataType.createVarChar(1000, DataType.ExaCharset.UTF8)));
    }

    @Test
    void testGetEmptyBehavior() {
        assertThat(this.sqlFunctionScalarJsonValue.getEmptyBehavior(), equalTo(this.emptyBehavior));
    }

    @Test
    void testGetErrorBehavior() {
        assertThat(this.sqlFunctionScalarJsonValue.getErrorBehavior(), equalTo(this.errorBehavior));
    }
}