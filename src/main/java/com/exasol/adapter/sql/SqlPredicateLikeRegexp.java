package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateLikeRegexp extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode pattern;

    public SqlPredicateLikeRegexp(final SqlNode left, final SqlNode pattern) {
        super(Predicate.REGEXP_LIKE);
        this.left = left;
        this.pattern = pattern;
        if (this.left != null) {
            this.left.setParent(this);
        }
        if (this.pattern != null) {
            this.pattern.setParent(this);
        }
    }

    public SqlNode getLeft() {
        return left;
    }

    public SqlNode getPattern() {
        return pattern;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LIKE_REGEXP;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}