package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * We could consider to apply builder pattern here (if time)
 */
public class SqlStatementSelect extends SqlStatement {
    private final SqlNode fromClause;
    private final SqlSelectList selectList;
    private final SqlNode whereClause;
    private final SqlExpressionList groupBy;
    private final SqlNode having;
    private final SqlOrderBy orderBy;
    private final SqlLimit limit;

    private SqlStatementSelect(final Builder builder) {
        this.fromClause = builder.fromClause;
        this.selectList = builder.selectList;
        this.whereClause = builder.whereClause;
        this.groupBy = builder.groupBy;
        this.having = builder.having;
        this.orderBy = builder.orderBy;
        this.limit = builder.limit;
        this.fromClause.setParent(this);
        this.selectList.setParent(this);

        if (this.whereClause != null) {
            this.whereClause.setParent(this);
        }
        if (this.groupBy != null) {
            this.groupBy.setParent(this);
        }
        if (this.having != null) {
            this.having.setParent(this);
        }
        if (this.orderBy != null) {
            this.orderBy.setParent(this);
        }
        if (this.limit != null) {
            this.limit.setParent(this);
        }
    }

    public boolean hasProjection() {
        return this.selectList != null;
    }

    public boolean hasGroupBy() {
        return this.groupBy != null;
    }

    public boolean hasHaving() {
        return this.having != null;
    }

    public boolean hasFilter() {
        return this.whereClause != null;
    }

    public boolean hasOrderBy() {
        return this.orderBy != null;
    }

    public boolean hasLimit() {
        return this.limit != null;
    }

    public SqlNode getFromClause() {
        return this.fromClause;
    }

    public SqlSelectList getSelectList() {
        return this.selectList;
    }

    public SqlNode getWhereClause() {
        return this.whereClause;
    }

    public SqlExpressionList getGroupBy() {
        return this.groupBy;
    }

    public SqlNode getHaving() {
        return this.having;
    }

    public SqlOrderBy getOrderBy() {
        return this.orderBy;
    }

    public SqlLimit getLimit() {
        return this.limit;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.SELECT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    /**
     * Create a new builder for {@link SqlStatementSelect}.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link SqlStatementSelect}.
     */
    public static class Builder {
        private SqlNode fromClause;
        private SqlSelectList selectList;
        private SqlNode whereClause;
        private SqlExpressionList groupBy;
        private SqlNode having;
        private SqlOrderBy orderBy;
        private SqlLimit limit;

        /**
         * Set the from clause of the SQL Select Statement.
         *
         * @param fromClause from clause
         * @return builder instance for fluent programming
         */
        public Builder fromClause(final SqlNode fromClause) {
            this.fromClause = fromClause;
            return this;
        }

        /**
         * Set the select list of the SQL Select Statement.
         *
         * @param selectList select list
         * @return builder instance for fluent programming
         */
        public Builder selectList(final SqlSelectList selectList) {
            this.selectList = selectList;
            return this;
        }

        /**
         * Set the where clause of the SQL Select Statement.
         *
         * @param whereClause where clause
         * @return builder instance for fluent programming
         */
        public Builder whereClause(final SqlNode whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        /**
         * Set the group by clause of the SQL Select Statement.
         *
         * @param groupBy group by clause
         * @return builder instance for fluent programming
         */
        public Builder groupBy(final SqlExpressionList groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        /**
         * Set the having clause of the SQL Select Statement.
         *
         * @param having having clause
         * @return builder instance for fluent programming
         */
        public Builder having(final SqlNode having) {
            this.having = having;
            return this;
        }

        /**
         * Set the order by clause of the SQL Select Statement.
         *
         * @param orderBy order by clause
         * @return builder instance for fluent programming
         */
        public Builder orderBy(final SqlOrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        /**
         * Set the limit clause of the SQL Select Statement.
         *
         * @param limit limit clause
         * @return builder instance for fluent programming
         */
        public Builder limit(final SqlLimit limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Build a new instance of {@link SqlStatementSelect}
         *
         * @return new instance
         */
        public SqlStatementSelect build() {
            return new SqlStatementSelect(this);
        }
    }
}