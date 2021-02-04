package com.exasol.adapter.sql;

import java.math.BigDecimal;

import com.exasol.adapter.AdapterException;

public class SqlLiteralExactnumeric extends SqlNode {
    private final BigDecimal value;

    public SqlLiteralExactnumeric(final BigDecimal value) {
        this.value = value;
    }

    /**
     * Get a value of the exactnumeric converted to a plain string without E notation.
     * <p>
     * For example: 1E-35 becomes 0.00000000000000000000000000000000001
     * </p>
     * 
     * @return exactnumeric value as a string
     */
    public String getValue() {
        return this.value.toPlainString();
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