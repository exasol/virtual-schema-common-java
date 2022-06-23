package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * {@code JOIN} node.
 */
public class SqlJoin extends SqlNode {
    private final SqlNode left;
    private final SqlNode right;
    private final SqlNode condition;
    private final JoinType joinType;

    /**
     * Instantiates a new Sql join.
     *
     * @param left      the left table
     * @param right     the right table
     * @param condition the join condition
     * @param joinType  the join type
     */
    public SqlJoin(final SqlNode left, final SqlNode right, final SqlNode condition, final JoinType joinType) {
        this.left = left;
        if (this.left != null) {
            this.left.setParent(this);
        }
        this.right = right;
        if (this.right != null) {
            this.right.setParent(this);
        }
        this.condition = condition;
        if (this.condition != null) {
            this.condition.setParent(this);
        }
        this.joinType = joinType;
    }

    /**
     * Gets left table.
     *
     * @return the left
     */
    public SqlNode getLeft() {
        return this.left;
    }

    /**
     * Gets right table.
     *
     * @return the right
     */
    public SqlNode getRight() {
        return this.right;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public SqlNode getCondition() {
        return this.condition;
    }

    /**
     * Gets join type.
     *
     * @return the joinType
     */
    public JoinType getJoinType() {
        return this.joinType;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.JOIN;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}