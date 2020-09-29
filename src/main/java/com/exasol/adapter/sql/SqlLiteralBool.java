package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralBool extends SqlNode {
    private final boolean value;

    public SqlLiteralBool(final boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_BOOL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SqlLiteralBool{" + "value=" + this.value + '}';
    }
}