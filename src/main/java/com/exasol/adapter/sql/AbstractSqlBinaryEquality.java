package com.exasol.adapter.sql;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract base for comparison operators with two operands.
 */
public abstract class AbstractSqlBinaryEquality extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode right;

    /**
     * Instantiates a new Abstract sql binary equality.
     *
     * @param function the function
     * @param left     the left
     * @param right    the right
     */
    public AbstractSqlBinaryEquality(final Predicate function, final SqlNode left, final SqlNode right) {
        super(function);
        this.left = left;
        this.right = right;
        if (this.left != null) {
            this.left.setParent(this);
        }
        if (this.right != null) {
            this.right.setParent(this);
        }
    }

    /**
     * Gets left.
     *
     * @return the left
     */
    public SqlNode getLeft() {
        return this.left;
    }

    /**
     * Gets right.
     *
     * @return the right
     */
    public SqlNode getRight() {
        return this.right;
    }

    @Override
    public List<SqlNode> getChildren() { return Arrays.asList(this.left, this.right); }
}