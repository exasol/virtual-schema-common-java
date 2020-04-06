package com.exasol.adapter.sql;

import java.util.*;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

/**
 * This class represents the {@link ScalarFunction#JSON_VALUE} scalar function.
 */
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
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("JSON_VALUE(");
        stringBuilder.append(getArguments().get(0).toSimpleSql());
        stringBuilder.append(", ");
        stringBuilder.append(getArguments().get(1).toSimpleSql());
        stringBuilder.append(" RETURNING ");
        stringBuilder.append(this.returningDataType.toString());
        stringBuilder.append(" ");
        stringBuilder.append(this.emptyBehavior.getBehaviorType());
        if (this.emptyBehavior.getExpression().isPresent()) {
            stringBuilder.append(" ");
            stringBuilder.append(this.emptyBehavior.getExpression().get());
        }
        stringBuilder.append(" ON EMPTY ");
        stringBuilder.append(this.errorBehavior.getBehaviorType());
        if (this.errorBehavior.getExpression().isPresent()) {
            stringBuilder.append(" ");
            stringBuilder.append(this.errorBehavior.getExpression().get().toSimpleSql());
        }
        stringBuilder.append(" ON ERROR)");
        return stringBuilder.toString();
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

    /**
     * A list of expected behavior types.
     */
    public enum BehaviorType {
        ERROR, NULL, DEFAULT
    }

    /**
     * This class represent behavior of {@link SqlFunctionScalarJsonValue} on error or empty.
     */
    public static class Behavior {
        private final BehaviorType behaviorType;
        private final Optional<SqlNode> expression;

        public Behavior(final BehaviorType behaviorType, final Optional<SqlNode> expression) {
            this.behaviorType = behaviorType;
            this.expression = expression;
        }

        public String getBehaviorType() {
            return this.behaviorType.name();
        }

        public Optional<SqlNode> getExpression() {
            return this.expression;
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            final Behavior behavior = (Behavior) object;
            return this.behaviorType == behavior.behaviorType && Objects.equals(this.expression, behavior.expression);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.behaviorType, this.expression);
        }
    }
}
