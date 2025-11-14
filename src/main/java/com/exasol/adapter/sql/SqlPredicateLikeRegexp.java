package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

/**
 * {@code REGEXP_LIKE} predicate.
 */
public class SqlPredicateLikeRegexp extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode pattern;

    /**
     * Instantiates a new Sql predicate like regexp.
     *
     * @param left    the left predicate
     * @param pattern the pattern
     */
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

    /**
     * Gets left.
     *
     * @return the left
     */
    public SqlNode getLeft() {
        return this.left;
    }

    /**
     * Gets pattern.
     *
     * @return the pattern
     */
    public SqlNode getPattern() {
        return this.pattern;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LIKE_REGEXP;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() { return List.of(this.left, this.pattern); }

}