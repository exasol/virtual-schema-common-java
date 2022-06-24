package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * The type Sql predicate less equal.
 */
public class SqlPredicateLessEqual extends AbstractSqlBinaryEquality {
    /**
     * Instantiates a new Sql predicate less equal.
     *
     * @param left  the left
     * @param right the right
     */
    public SqlPredicateLessEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.LESSEQUAL, left, right);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LESSEQUAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}