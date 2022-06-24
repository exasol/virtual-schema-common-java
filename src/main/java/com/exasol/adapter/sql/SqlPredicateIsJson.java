package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * This class represents the {@link Predicate#IS_JSON} predicate.
 */
public class SqlPredicateIsJson extends AbstractSqlPredicateJson {
    /**
     * Instantiates a new Sql predicate is json.
     *
     * @param expression              the expression
     * @param typeConstraint          the type constraint
     * @param keyUniquenessConstraint the key uniqueness constraint
     */
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
}