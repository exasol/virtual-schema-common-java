package com.exasol.adapter.sql;

import java.util.*;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import com.exasol.errorreporting.ExaError;

/**
 * This class represents the {@link ScalarFunction#JSON_VALUE} scalar function.
 */
public class SqlFunctionScalarJsonValue extends SqlNode {
    private final ScalarFunction scalarFunction;
    private final List<SqlNode> arguments;
    private final DataType returningDataType;
    private final Behavior emptyBehavior;
    private final Behavior errorBehavior;

    /**
     * Instantiates a new Sql function scalar json value.
     *
     * @param scalarFunction    the scalar function
     * @param arguments         the arguments
     * @param returningDataType the returning data type
     * @param emptyBehavior     the empty behavior
     * @param errorBehavior     the error behavior
     */
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
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-26").message(
                    "Invalid function name for function_scalar_json_value: {{functionName}}. Only JSON_VALUE is supported.")
                    .parameter("functionName", scalarFunction.name()).toString());
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

    /**
     * Gets scalar function.
     *
     * @return the scalar function
     */
    public ScalarFunction getScalarFunction() {
        return this.scalarFunction;
    }

    /**
     * Gets arguments.
     *
     * @return the arguments
     */
    public List<SqlNode> getArguments() {
        return this.arguments;
    }

    /**
     * Gets returning data type.
     *
     * @return the returning data type
     */
    public DataType getReturningDataType() {
        return this.returningDataType;
    }

    /**
     * Gets empty behavior.
     *
     * @return the empty behavior
     */
    public Behavior getEmptyBehavior() {
        return this.emptyBehavior;
    }

    /**
     * Gets error behavior.
     *
     * @return the error behavior
     */
    public Behavior getErrorBehavior() {
        return this.errorBehavior;
    }

    /**
     * A list of expected behavior types.
     */
    public enum BehaviorType {
        /**
         * Error behavior type.
         */
        ERROR,
        /**
         * Null behavior type.
         */
        NULL,
        /**
         * Default behavior type.
         */
        DEFAULT
    }

    /**
     * This class represent behavior of {@link SqlFunctionScalarJsonValue} on error or empty.
     */
    public static class Behavior {
        private final BehaviorType behaviorType;
        private final Optional<SqlNode> expression;

        /**
         * Instantiates a new Behavior.
         *
         * @param behaviorType the behavior type
         * @param expression   the expression
         */
        public Behavior(final BehaviorType behaviorType, final Optional<SqlNode> expression) {
            this.behaviorType = behaviorType;
            this.expression = expression;
        }

        /**
         * Gets behavior type.
         *
         * @return the behavior type
         */
        public String getBehaviorType() {
            return this.behaviorType.name();
        }

        /**
         * Gets expression.
         *
         * @return the expression
         */
        public Optional<SqlNode> getExpression() {
            return this.expression;
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Behavior)) {
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

    @Override
    public List<SqlNode> getChildren() { return getArguments(); }

}