package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Date literal.
 */
public class SqlLiteralDate extends SqlNode {
    private final String value;

    /**
     * Instantiates a new SQL literal date.
     *
     * @param value the date value as string
     */
    public SqlLiteralDate(final String value) {
        this.value = value;
    }

    /**
     * Get the value.
     * 
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