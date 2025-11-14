package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

/**
 * {@code LIKE} predicate
 */
public class SqlPredicateLike extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode pattern;
    private final SqlNode escapeChar;

    /**
     * Instantiates a new Sql predicate like.
     *
     * @param left    the left
     * @param pattern the pattern
     */
    public SqlPredicateLike(final SqlNode left, final SqlNode pattern) {
        this(left, pattern, null);
    }

    /**
     * Instantiates a new Sql predicate like.
     *
     * @param left       the left
     * @param pattern    the pattern
     * @param escapeChar the escape char
     */
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

    /**
     * Gets escape char.
     *
     * @return the escape char
     */
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

    @Override
    public List<SqlNode> getChildren() { return List.of(this.left, this.pattern, this.escapeChar); }
}