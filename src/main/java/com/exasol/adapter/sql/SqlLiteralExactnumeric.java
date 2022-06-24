package com.exasol.adapter.sql;

import java.math.BigDecimal;

import com.exasol.adapter.AdapterException;

/**
 * Literal for decimal types.
 */
public class SqlLiteralExactnumeric extends SqlNode {
    private final BigDecimal value;

    /**
     * Instantiates a new Sql literal exactnumeric.
     *
     * @param value exact numeric value
     */
    public SqlLiteralExactnumeric(final BigDecimal value) {
        this.value = value;
    }

    /**
     * Get the value.
     * 
     * @return underlying exactnumeric value as a BigDecimal
     */
    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_EXACTNUMERIC;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
