package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

public class SqlFunctionAggregateGroupConcat extends SqlNode {
    private final AggregateFunction function;
    private final boolean distinct;
    private final List<SqlNode> arguments;
    private final String separator;
    private final SqlOrderBy orderBy;

    public SqlFunctionAggregateGroupConcat(final AggregateFunction function, final List<SqlNode> arguments,
            final SqlOrderBy orderBy, final boolean distinct, final String separator) {
        SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionAggregateGroupConcat.class);
        this.function = function;
        this.distinct = distinct;
        this.arguments = arguments;
        this.orderBy = orderBy;
        this.separator = separator;

        for (final SqlNode node : this.arguments) {
            node.setParent(this);
        }
        if (orderBy != null) {
            orderBy.setParent(this);
        }
    }

    public AggregateFunction getFunction() {
        return this.function;
    }

    public List<SqlNode> getArguments() {
        if (this.arguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.arguments);
        }
    }

    public boolean hasOrderBy() {
        return this.orderBy != null && this.orderBy.getExpressions() != null
                && !this.orderBy.getExpressions().isEmpty();
    }

    public SqlOrderBy getOrderBy() {
        return this.orderBy;
    }

    public String getFunctionName() {
        return this.function.name();
    }

    public String getSeparator() {
        return this.separator;
    }

    public boolean hasDistinct() {
        return this.distinct;
    }

    @Override
    public String toSimpleSql() {
        String distinctSql = "";
        if (this.distinct) {
            distinctSql = "DISTINCT ";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(getFunctionName());
        builder.append("(");
        builder.append(distinctSql);
        assert this.arguments != null;
        assert this.arguments.size() == 1 && this.arguments.get(0) != null;
        builder.append(this.arguments.get(0).toSimpleSql());
        if (this.orderBy != null) {
            builder.append(" ");
            builder.append(this.orderBy.toSimpleSql());
        }
        if (this.separator != null) {
            builder.append(" SEPARATOR ");
            builder.append("'");
            builder.append(this.separator);
            builder.append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_AGGREGATE_GROUP_CONCAT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}