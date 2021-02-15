package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralDouble extends SqlNode {
    private final double value;

    public SqlLiteralDouble(final double value) {
        this.value = value;
    }

    /**
     * 
     * @return underlying value as double
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
