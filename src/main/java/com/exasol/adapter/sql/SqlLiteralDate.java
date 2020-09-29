package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralDate extends SqlNode {
    private final String value;

    public SqlLiteralDate(final String value) {
        this.value = value;
    }

    /**
     * @return literal date value in format: YYYY-MM-DD
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_DATE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}