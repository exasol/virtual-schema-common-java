package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralNull extends SqlNode {
    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_NULL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}