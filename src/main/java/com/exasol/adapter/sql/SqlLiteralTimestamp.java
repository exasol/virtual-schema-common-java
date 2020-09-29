package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralTimestamp extends SqlNode {
    private final String value;

    public SqlLiteralTimestamp(final String value) {
        this.value = value;
    }

    /**
     * @return literal timestamp value in format: YYYY-MM-DD HH:MI:SS.FF6
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_TIMESTAMP;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}