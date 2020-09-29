package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlJoin extends SqlNode {
    private final SqlNode left;
    private final SqlNode right;
    private final SqlNode condition;
    private final JoinType joinType;

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
     * @return the left
     */
    public SqlNode getLeft() {
        return this.left;
    }

    /**
     * @return the right
     */
    public SqlNode getRight() {
        return this.right;
    }

    /**
     * @return the condition
     */
    public SqlNode getCondition() {
        return this.condition;
    }

    /**
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