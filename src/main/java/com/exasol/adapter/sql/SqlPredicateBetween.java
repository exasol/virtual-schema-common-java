package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateBetween extends SqlPredicate {
    private final SqlNode expression;
    private final SqlNode betweenLeft;
    private final SqlNode betweenRight;

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

    public SqlNode getExpression() {
        return expression;
    }

    public SqlNode getBetweenLeft() {
        return betweenLeft;
    }

    public SqlNode getBetweenRight() {
        return betweenRight;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_BETWEEN;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}