package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateIsNotJson extends AbstractSqlPredicateJson {
    public SqlPredicateIsNotJson(final SqlNode expression,
            final AbstractSqlPredicateJson.TypeConstraints typeConstraint,
            final AbstractSqlPredicateJson.KeyUniquenessConstraint keyUniquenessConstraint) {
        super(Predicate.IS_NOT_JSON, expression, typeConstraint, keyUniquenessConstraint);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IS_NOT_JSON;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    String toSimpleSql() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.expression.toSimpleSql());
        stringBuilder.append(" IS NOT JSON ");
        stringBuilder.append(getTypeConstraint());
        stringBuilder.append(" ");
        stringBuilder.append(getKeyUniquenessConstraint());
        return stringBuilder.toString();
    }
}
