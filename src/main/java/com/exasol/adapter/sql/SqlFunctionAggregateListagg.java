package com.exasol.adapter.sql;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.exasol.adapter.AdapterException;

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
     * @return argument
     */
    public SqlNode getArgument() {
        return this.argument;
    }

    /**
     * Get a separator.
     * 
     * @return separator
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
     * Expected behavior types.
     */
    public enum BehaviorType {
        ERROR, TRUNCATE
    }

    /**
     * This class represent behavior of {@link SqlFunctionAggregateListagg}.
     */
    public static class Behavior {
        private final BehaviorType behaviorType;
        private TruncationType truncationType = null;
        private SqlLiteralString truncationFiller = null;

        /**
         * Expected truncation types.
         */
        public enum TruncationType {
            WITH_COUNT, WITHOUT_COUNT;

            public static TruncationType parseTruncationType(final String value) {
                if (value.equalsIgnoreCase("WITH COUNT")) {
                    return WITH_COUNT;
                } else if (value.equalsIgnoreCase("WITHOUT COUNT")) {
                    return WITHOUT_COUNT;
                } else {
                    throw new IllegalArgumentException("Illegal value " + value
                            + " was set for a 'truncation type' parameter of the LISTAGG function. Possible values: "
                            + Arrays.stream(TruncationType.values()).map(TruncationType::toString)
                                    .collect(Collectors.joining(", ")));
                }
            }

            @Override
            public String toString() {
                return name().replace("_", " ");
            }
        }

        /**
         * Create a new instance of {@link Behavior}.
         * 
         * @param behaviorType behavior type
         */
        public Behavior(final BehaviorType behaviorType) {
            this.behaviorType = behaviorType;
        }

        /**
         * Get a truncation type.
         * 
         * @return truncation type
         */
        public String getTruncationType() {
            return this.truncationType.toString();
        }

        /**
         * Set a truncation type.
         * 
         * @param truncationType truncation type
         */
        public void setTruncationType(final TruncationType truncationType) {
            this.truncationType = truncationType;
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
         * Check if a truncation filler exists.
         *
         * @return true if a truncation filler exists
         */
        public boolean hasTruncationFiller() {
            return this.truncationFiller != null;
        }

        /**
         * Set a truncation filler.
         * 
         * @param truncationFiller truncation filler
         */
        public void setTruncationFiller(final SqlLiteralString truncationFiller) {
            this.truncationFiller = truncationFiller;
        }

        /**
         * Get a behavior type.
         * 
         * @return behavior type
         */
        public BehaviorType getBehaviorType() {
            return this.behaviorType;
        }
    }
}