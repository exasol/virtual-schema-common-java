package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Less predicate.
 */
public class SqlPredicateLess extends AbstractSqlBinaryEquality {
    /**
     * Instantiates a new Sql predicate less.
     *
     * @param left  the left operand
     * @param right the right operand
     */
    public SqlPredicateLess(final SqlNode left, final SqlNode right) {
        super(Predicate.LESS, left, right);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_LESS;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}