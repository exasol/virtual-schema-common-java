package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

/**
 * Between predicate.
 */
public class SqlPredicateBetween extends SqlPredicate {
    private final SqlNode expression;
    private final SqlNode betweenLeft;
    private final SqlNode betweenRight;

    /**
     * Instantiates a new Sql predicate between.
     *
     * @param expression   the expression
     * @param betweenLeft  the between left
     * @param betweenRight the between right
     */
    public SqlPredicateBetween(final SqlNode expression, final SqlNode betweenLeft, final SqlNode betweenRight) {
        super(Predicate.BETWEEN);
        this.expression = expression;
        this.betweenLeft = betweenLeft;
        this.betweenRight = betweenRight;
        if (this.expression != null) {
            this.expression.setParent(this);
        }
        if (this.betweenLeft != null) {
            this.betweenLeft.setParent(this);
        }
        if (this.betweenRight != null) {
            this.betweenRight.setParent(this);
        }
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public SqlNode getExpression() {
        return this.expression;
    }

    /**
     * Gets between left.
     *
     * @return the between left
     */
    public SqlNode getBetweenLeft() {
        return this.betweenLeft;
    }

    /**
     * Gets between right.
     *
     * @return the between right
     */
    public SqlNode getBetweenRight() {
        return this.betweenRight;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_BETWEEN;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() { return List.of(this.expression, this.betweenLeft, this.betweenRight); }
}