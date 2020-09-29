package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

public class SqlGroupBy extends SqlExpressionList {

    public SqlGroupBy(final List<SqlNode> groupByList) {
        super(groupByList);
        if (this.getExpressions() != null) {
            for (final SqlNode node : this.getExpressions()) {
                node.setParent(this);
            }
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.GROUP_BY;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}