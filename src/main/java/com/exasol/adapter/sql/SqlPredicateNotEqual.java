package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateNotEqual extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode right;
    
    public SqlPredicateNotEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.NOTEQUAL);
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
        return left.toSimpleSql() + " != " + right.toSimpleSql();
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_NOTEQUAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
