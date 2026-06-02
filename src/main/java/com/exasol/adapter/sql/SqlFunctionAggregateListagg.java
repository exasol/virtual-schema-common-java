package com.exasol.adapter.sql;

import java.util.*;
import java.util.stream.Collectors;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

/**
 * Represents a LISTAGG aggregate function.
 */
public class SqlFunctionAggregateListagg extends SqlNode {
    private static final AggregateFunction function = AggregateFunction.LISTAGG;
    private final boolean distinct;
    private final SqlNode argument;
    private final SqlLiteralString separator;
    private final SqlOrderBy orderBy;
    private final Behavior overflowBehavior;

    private SqlFunctionAggregateListagg(final Builder builder) {
        this.distinct = builder.distinct;
        this.argument = builder.argument;
        this.orderBy = builder.orderBy;
        this.separator = builder.separator;
        this.overflowBehavior = builder.overflowBehavior;
        this.argument.setParent(this);
        if (this.orderBy != null) {
            this.orderBy.setParent(this);
        }
        if (this.separator != null) {
            this.separator.setParent(this);
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_AGGREGATE_LISTAGG;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    /**
     * Check if the listagg function contains distinct.
     *
     * @return true if contains distinct
     */
    public boolean hasDistinct() {
        return this.distinct;
    }

    /**
     * Check if the listagg function contains an order by clause.
     *
     * @return true if contains an order by clause
     */
    public boolean hasOrderBy() {
        return this.orderBy != null && this.orderBy.getExpressions() != null
                && !this.orderBy.getExpressions().isEmpty();
    }

    /**
     * Check if the listagg function contains a separator.
     *
     * @return true if contains a separator
     */
    public boolean hasSeparator() {
        return this.separator != null;
    }

    /**
     * Get a function argument.
     *
     * @return argument argument
     */
    public SqlNode getArgument() {
        return this.argument;
    }

    /**
     * Get a separator.
     *
     * @return separator separator
     */
    public SqlLiteralString getSeparator() {
        return this.separator;
    }

    /**
     * Get an order by clause.
     *
     * @return order by clause
     */
    public SqlOrderBy getOrderBy() {
        return this.orderBy;
    }

    /**
     * Get an overflow behavior.
     *
     * @return overflow behavior
     */
    public Behavior getOverflowBehavior() {
        return this.overflowBehavior;
    }

    /**
     * Get a function name.
     *
     * @return function name as a string
     */
    public String getFunctionName() {
        return function.name();
    }

    /**
     * Get a {@link SqlFunctionAggregateListagg} builder.
     *
     * @param argument         function argument
     * @param overflowBehavior overflow behavior
     * @return builder instance
     */
    public static Builder builder(final SqlNode argument, final Behavior overflowBehavior) {
        return new Builder(argument, overflowBehavior);
    }

    /**
     * Expected behavior types.
     */
    public enum BehaviorType {
        /**
         * Error behavior type.
         */
        ERROR,
        /**
         * Truncate behavior type.
         */
        TRUNCATE
    }

    /**
     * Builder for {@link SqlFunctionAggregateListagg}.
     */
    public static final class Builder {
        private final SqlNode argument;
        private final Behavior overflowBehavior;
        private boolean distinct = false;
        private SqlOrderBy orderBy = null;
        private SqlLiteralString separator = null;

        private Builder(final SqlNode argument, final Behavior overflowBehavior) {
            this.argument = argument;
            this.overflowBehavior = overflowBehavior;
        }

        /**
         * Create new {@link SqlFunctionAggregateListagg} instance.
         *
         * @return new {@link SqlFunctionAggregateListagg} instance
         */
        public SqlFunctionAggregateListagg build() {
            return new SqlFunctionAggregateListagg(this);
        }

        /**
         * Add a distinct.
         *
         * @param distinct distinct
         * @return builder instance for fluent programming
         */
        public Builder distinct(final boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        /**
         * Add an order by clause.
         *
         * @param orderBy order by clause
         * @return builder instance for fluent programming
         */
        public Builder orderBy(final SqlOrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        /**
         * Add a separator.
         *
         * @param separator separator
         * @return builder instance for fluent programming
         */
        public Builder separator(final SqlLiteralString separator) {
            this.separator = separator;
            return this;
        }
    }

    /**
     * This class represent behavior of {@link SqlFunctionAggregateListagg}.
     */
    public static final class Behavior {
        private final BehaviorType behaviorType;
        private final TruncationType truncationType;
        private final SqlLiteralString truncationFiller;

        /**
         * Create a new instance of {@link Behavior}.
         *
         * @param behaviorType behavior type
         */
        public Behavior(final BehaviorType behaviorType) {
            this(behaviorType, null, null);
        }

        /**
         * Create a new fully initialized instance of {@link Behavior}.
         *
         * @param behaviorType     behavior type
         * @param truncationType   truncation type
         * @param truncationFiller truncation filler
         */
        public Behavior(final BehaviorType behaviorType, final TruncationType truncationType,
                final SqlLiteralString truncationFiller) {
            this.behaviorType = behaviorType;
            this.truncationType = truncationType;
            this.truncationFiller = truncationFiller;
        }

        /**
         * Get a truncation type.
         *
         * @return truncation type
         */
        public String getTruncationType() {
            return this.truncationType == null ? null : this.truncationType.toString();
        }

        /**
         * Set a truncation type.
         *
         * @param truncationType truncation type
         * @deprecated The behavior of the LISTAGG function is immutable and can not be changed after the creation of the object.
         */
        @Deprecated(since = "18.0.2", forRemoval = true)
        public void setTruncationType(final TruncationType truncationType) {
            throw new UnsupportedOperationException("LISTAGG overflow behavior is immutable.");
        }

        /**
         * Get a truncation filler.
         *
         * @return truncation filler
         */
        public SqlLiteralString getTruncationFiller() {
            return this.truncationFiller;
        }

        /**
         * Set a truncation filler.
         *
         * @param truncationFiller truncation filler
         * @deprecated The behavior of the LISTAGG function is immutable and can not be changed after the creation of the object.
         */
        @Deprecated(since = "18.0.2", forRemoval = true)
        public void setTruncationFiller(final SqlLiteralString truncationFiller) {
            throw new UnsupportedOperationException("LISTAGG overflow behavior is immutable.");
        }

        /**
         * Check if a truncation filler exists.
         *
         * @return true if a truncation filler exists
         */
        public boolean hasTruncationFiller() {
            return this.truncationFiller != null;
        }

        /**
         * Get a behavior type.
         *
         * @return behavior type
         */
        public BehaviorType getBehaviorType() {
            return this.behaviorType;
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
            return this.behaviorType == behavior.behaviorType
                    && this.truncationType == behavior.truncationType
                    && Objects.equals(this.truncationFiller, behavior.truncationFiller);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.behaviorType, this.truncationType, this.truncationFiller);
        }

        /**
         * Expected truncation types.
         */
        public enum TruncationType {
            /**
             * With count truncation type.
             */
            WITH_COUNT,
            /**
             * Without count truncation type.
             */
            WITHOUT_COUNT;

            /**
             * Parse truncation type.
             *
             * @param value truncation type string
             * @return the truncation type
             */
            public static TruncationType parseTruncationType(final String value) {
                if (value.equalsIgnoreCase("WITH COUNT")) {
                    return WITH_COUNT;
                } else if (value.equalsIgnoreCase("WITHOUT COUNT")) {
                    return WITHOUT_COUNT;
                } else {
                    throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-25").message(
                            "Illegal value {{value}} was set for a 'truncation type' parameter of the LISTAGG function. "
                                    + "Possible values: {{possibleValues|uq}}.")
                            .parameter("value", value)
                            .parameter("possibleValues", Arrays.stream(TruncationType.values())
                                    .map(TruncationType::toString).collect(Collectors.joining(", ")))
                            .toString());
                }
            }

            @Override
            public String toString() {
                return name().replace("_", " ");
            }
        }
    }

    @Override
    public List<SqlNode> getChildren() {
        return Arrays.asList(this.argument);
    }
}
