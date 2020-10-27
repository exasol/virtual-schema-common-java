package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Represents a GROUP_CONCAT aggregate function.
 */
public class SqlFunctionAggregateGroupConcat extends SqlNode {
    private static final AggregateFunction function = AggregateFunction.GROUP_CONCAT;
    private final SqlNode argument;
    private final boolean distinct;
    private final SqlLiteralString separator;
    private final SqlOrderBy orderBy;

    private SqlFunctionAggregateGroupConcat(final Builder builder) {
        this.argument = builder.argument;
        this.distinct = builder.distinct;
        this.orderBy = builder.orderBy;
        this.separator = builder.separator;
        this.argument.setParent(this);
        if (this.orderBy != null) {
            this.orderBy.setParent(this);
        }
        if (this.separator != null) {
            this.separator.setParent(this);
        }
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
     * Check if the this function contains an order by clause.
     *
     * @return true if contains an order by clause
     */
    public boolean hasOrderBy() {
        return this.orderBy != null && this.orderBy.getExpressions() != null
                && !this.orderBy.getExpressions().isEmpty();
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
     * Get a function name.
     *
     * @return function name as a string
     */
    public String getFunctionName() {
        return function.name();
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
     * Get a separator.
     *
     * @return separator
     */
    public SqlLiteralString getSeparator() {
        return this.separator;
    }

    /**
     * Check if the listagg function contains distinct.
     *
     * @return true if contains distinct
     */
    public boolean hasDistinct() {
        return this.distinct;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_AGGREGATE_GROUP_CONCAT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    /**
     * Get a {@link SqlFunctionAggregateGroupConcat} builder.
     *
     * @param argument function argument
     * @return builder instance
     */
    public static Builder builder(final SqlNode argument) {
        return new Builder(argument);
    }

    /**
     * Builder for {@link SqlFunctionAggregateGroupConcat}.
     */
    public static final class Builder {
        private final SqlNode argument;
        private boolean distinct = false;
        private SqlOrderBy orderBy = null;
        private SqlLiteralString separator = null;

        private Builder(final SqlNode argument) {
            this.argument = argument;
        }

        /**
         * Create new {@link SqlFunctionAggregateGroupConcat} instance.
         *
         * @return new {@link SqlFunctionAggregateGroupConcat} instance
         */
        public SqlFunctionAggregateGroupConcat build() {
            return new SqlFunctionAggregateGroupConcat(this);
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
}