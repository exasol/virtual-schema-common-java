package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Equal predicate.
 */
public class SqlPredicateEqual extends AbstractSqlBinaryEquality {
    /**
     * Instantiates a new Sql predicate equal.
     *
     * @param left  the left operand
     * @param right the right operand
     */
    public SqlPredicateEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.EQUAL, left, right);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_EQUAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}