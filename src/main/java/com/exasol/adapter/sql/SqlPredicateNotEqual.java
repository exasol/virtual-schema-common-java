package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateNotEqual extends AbstractSqlBinaryEquality {
    public SqlPredicateNotEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.NOTEQUAL, left, right);
    }

    @Override
    public String toSimpleSql() {
        return super.toSimpleSql(" != ");
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
