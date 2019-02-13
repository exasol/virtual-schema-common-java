package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlPredicateLess extends AbstractSqlBinaryEquality {
    public SqlPredicateLess(final SqlNode left, final SqlNode right) {
        super(Predicate.LESS, left, right);
    }

    @Override
    public String toSimpleSql() {
        return super.toSimpleSql(" < ");
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
