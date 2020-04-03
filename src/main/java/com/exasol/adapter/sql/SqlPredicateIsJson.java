package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateIsJson extends AbstractSqlPredicateJson {
    public SqlPredicateIsJson(final SqlNode expression, final TypeConstraints typeConstraint,
            final KeyUniquenessConstraint keyUniquenessConstraint) {
        super(Predicate.IS_JSON, expression, typeConstraint, keyUniquenessConstraint);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IS_JSON;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    String toSimpleSql() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.expression.toSimpleSql());
        stringBuilder.append(" IS JSON ");
        stringBuilder.append(getTypeConstraint());
        stringBuilder.append(" ");
        stringBuilder.append(getKeyUniquenessConstraint());
        return stringBuilder.toString();
    }
}
