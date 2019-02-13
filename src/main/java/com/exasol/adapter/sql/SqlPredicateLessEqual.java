package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateLessEqual extends AbstractSqlBinaryEquality {
    public SqlPredicateLessEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.LESSEQUAL, left, right);
    }

    @Override
    public String toSimpleSql() {
        return super.toSimpleSql(" <= ");
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
