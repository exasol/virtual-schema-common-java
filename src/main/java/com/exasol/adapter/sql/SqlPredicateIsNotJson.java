package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * This class represents the {@link Predicate#IS_NOT_JSON} predicate.
 */
public class SqlPredicateIsNotJson extends AbstractSqlPredicateJson {
    public SqlPredicateIsNotJson(final SqlNode expression, final TypeConstraints typeConstraint,
            final KeyUniquenessConstraint keyUniquenessConstraint) {
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
}