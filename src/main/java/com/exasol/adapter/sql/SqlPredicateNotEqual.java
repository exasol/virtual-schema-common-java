package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Not equal predicate.
 */
public class SqlPredicateNotEqual extends AbstractSqlBinaryEquality {
    /**
     * Instantiates a new Sql predicate not equal.
     *
     * @param left  the left predicate
     * @param right the right predicate
     */
    public SqlPredicateNotEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.NOTEQUAL, left, right);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_NOTEQUAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}