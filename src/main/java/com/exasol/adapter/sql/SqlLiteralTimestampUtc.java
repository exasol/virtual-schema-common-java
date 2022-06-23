package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Timestamp UTC literal.
 */
public class SqlLiteralTimestampUtc extends SqlNode {
    private final String value;

    /**
     * Instantiates a new Sql literal timestamp utc.
     *
     * @param value the value
     */
    public SqlLiteralTimestampUtc(final String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return literal timestamp utc value in format: YYYY-MM-DD HH:MI:SS.FF6
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_TIMESTAMPUTC;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}