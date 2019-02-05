package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateLess extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode right;
    
    public SqlPredicateLess(final SqlNode left, final SqlNode right) {
        super(Predicate.LESS);
        this.left = left;
        this.right = right;
        if (this.left != null) {
            this.left.setParent(this);
        }
        if (this.right != null) {
            this.right.setParent(this);
        }
    }
    
    public SqlNode getLeft() {
        return left;
    }
    
    public SqlNode getRight() {
        return right;
    }
    
    @Override
    public String toSimpleSql() {
        return left.toSimpleSql() + " < " + right.toSimpleSql();
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LESS;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
