package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateEqual extends AbstractSqlBinaryEquality {
    public SqlPredicateEqual(final SqlNode left, final SqlNode right) {
        super(Predicate.EQUAL, left, right);
    }

    @Override
    public String toSimpleSql() {
        return super.toSimpleSql(" = ");
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
