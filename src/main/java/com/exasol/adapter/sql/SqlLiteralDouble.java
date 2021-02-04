package com.exasol.adapter.sql;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.exasol.adapter.AdapterException;

public class SqlLiteralDouble extends SqlNode {
    private final double value;

    public SqlLiteralDouble(final double value) {
        this.value = value;
    }

    /**
     * Get a value of the double converted to an E-notation format.
     * <p>
     * For example: 1.234 becomes 1.234E0
     * </p>
     *
     * @return double value as a string
     */
    public String getValue() {
        final NumberFormat numFormat = new DecimalFormat("0.################E0");
        return numFormat.format(this.value);
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_DOUBLE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}