package com.exasol.adapter.sql;

import java.util.List;
import java.util.Optional;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

public class SqlFunctionScalarJsonValue extends SqlNode {
    private final ScalarFunction scalarFunction;
    private final List<SqlNode> arguments;
    private final DataType returningDataType;
    private final Behavior emptyBehavior;
    private final Behavior errorBehavior;

    public SqlFunctionScalarJsonValue(final ScalarFunction scalarFunction, final List<SqlNode> arguments,
            final DataType returningDataType, final Behavior emptyBehavior, final Behavior errorBehavior) {
        validateFunctionName(scalarFunction);
        this.scalarFunction = scalarFunction;
        this.arguments = arguments;
        this.returningDataType = returningDataType;
        this.emptyBehavior = emptyBehavior;
        this.errorBehavior = errorBehavior;
    }

    private void validateFunctionName(final ScalarFunction scalarFunction) {
        if (scalarFunction != ScalarFunction.JSON_VALUE) {
            throw new IllegalArgumentException(
                    "Invalid function name for function_scalar_json_value. Only JSON_VALUE is supported.");
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_JSON_VALUE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    String toSimpleSql() {
        return null;
    }

    public ScalarFunction getScalarFunction() {
        return this.scalarFunction;
    }

    public List<SqlNode> getArguments() {
        return this.arguments;
    }

    public DataType getReturningDataType() {
        return this.returningDataType;
    }

    public Behavior getEmptyBehavior() {
        return this.emptyBehavior;
    }

    public Behavior getErrorBehavior() {
        return this.errorBehavior;
    }

    public enum BehaviorType {
        ERROR, NULL, DEFAULT
    }

    public static class Behavior {
        private BehaviorType behaviorType;

        private Optional<SqlNode> expression;

        public Behavior(final BehaviorType behaviorType, final Optional<SqlNode> expression) {
            this.behaviorType = behaviorType;
            this.expression = expression;
        }

        protected BehaviorType getBehaviorType() {
            return behaviorType;
        }

        public Optional<SqlNode> getExpression() {
            return expression;
        }
    }
}
