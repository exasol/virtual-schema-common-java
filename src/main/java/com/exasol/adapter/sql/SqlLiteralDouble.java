package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralDouble extends SqlNode {
    private final double value;

    public SqlLiteralDouble(final double value) {
        this.value = value;
    }

    /**
     * Get a value of the double converted to an E-notation format.
     * 
     * @return double value as a string
     */
    public double getValue() {
        return this.value;
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