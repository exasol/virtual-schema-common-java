package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

/**
 * Represents a GROUP BY statement.
 */
public class SqlGroupBy extends SqlExpressionList {
    private final boolean singleGroupAggregation;

    /**
     * Create a new instance of {@link SqlGroupBy}.
     * 
     * @param groupByList            list of expressions
     * @param singleGroupAggregation true if the aggregation is a single group
     */
    public SqlGroupBy(final List<SqlNode> groupByList, final boolean singleGroupAggregation) {
        super(groupByList);
        this.singleGroupAggregation = singleGroupAggregation;
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

    /**
     * Check if this group by is a single group aggregation.
     * 
     * @return true if this group by is a single group aggregation
     */
    public boolean isSingleGroupAggregation() {
        return this.singleGroupAggregation;
    }
}