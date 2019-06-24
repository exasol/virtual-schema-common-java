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

    public SqlStatementSelect(final SqlNode fromClause, final SqlSelectList selectList, final SqlNode whereClause,
            final SqlExpressionList groupBy, final SqlNode having, final SqlOrderBy orderBy, final SqlLimit limit) {
        this.fromClause = fromClause;
        this.selectList = selectList;
        this.whereClause = whereClause;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        assert this.fromClause != null;
        assert this.selectList != null;
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
    public String toSimpleSql() {

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(this.selectList.toSimpleSql());
        sql.append(" FROM ");
        sql.append(this.fromClause.toSimpleSql());
        if (hasFilter()) {
            sql.append(" WHERE ");
            sql.append(this.whereClause.toSimpleSql());
        }
        if (hasGroupBy()) {
            sql.append(" GROUP BY ");
            sql.append(this.groupBy.toSimpleSql());
        }
        if (hasHaving()) {
            sql.append(" HAVING ");
            sql.append(this.having.toSimpleSql());
        }
        if (hasOrderBy()) {
            sql.append(" ");
            sql.append(this.orderBy.toSimpleSql());
        }
        if (hasLimit()) {
            sql.append(" ");
            sql.append(this.limit.toSimpleSql());
        }
        return sql.toString();
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.SELECT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
