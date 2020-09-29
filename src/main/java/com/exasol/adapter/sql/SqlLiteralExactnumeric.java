package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.math.BigDecimal;

public class SqlLiteralExactnumeric extends SqlNode {
    private final BigDecimal value;

    public SqlLiteralExactnumeric(final BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
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