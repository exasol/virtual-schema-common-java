package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateIsNull extends SqlPredicate {
    private final SqlNode expression;

    public SqlPredicateIsNull(final SqlNode expression) {
        super(Predicate.IS_NULL);
        this.expression = expression;
        if (this.expression != null) {
            this.expression.setParent(this);
        }
    }

    public SqlNode getExpression() {
        return expression;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IS_NULL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}