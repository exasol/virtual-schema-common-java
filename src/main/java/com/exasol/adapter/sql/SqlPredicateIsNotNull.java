package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateIsNotNull extends SqlPredicate {
    private final SqlNode expression;

    public SqlPredicateIsNotNull(final SqlNode expression) {
        super(Predicate.IS_NULL);
        this.expression = expression;
        if (this.expression != null) {
            this.expression.setParent(this);
        }
    }

    public SqlNode getExpression() {
        return this.expression;
    }

    @Override
    public String toSimpleSql() {
        return this.expression.toSimpleSql() + " IS NOT NULL";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IS_NOT_NULL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
