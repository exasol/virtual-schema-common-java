package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateLike extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode pattern;
    private final SqlNode escapeChar;

    public SqlPredicateLike(final SqlNode left, final SqlNode pattern) {
        this(left, pattern, null);
    }

    public SqlPredicateLike(final SqlNode left, final SqlNode pattern, final SqlNode escapeChar) {
        super(Predicate.LIKE);
        this.left = left;
        this.pattern = pattern;
        this.escapeChar = escapeChar;
        if (this.left != null) {
            this.left.setParent(this);
        }
        if (this.pattern != null) {
            this.pattern.setParent(this);
        }
        if (this.escapeChar != null) {
            this.escapeChar.setParent(this);
        }
    }

    public SqlNode getLeft() {
        return this.left;
    }

    public SqlNode getPattern() {
        return this.pattern;
    }

    public SqlNode getEscapeChar() {
        return this.escapeChar;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LIKE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}