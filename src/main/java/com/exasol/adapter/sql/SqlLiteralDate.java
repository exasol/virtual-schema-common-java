package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralDate extends SqlNode {
    //Format: YYYY-MM-DD
    private final String value;
    
    public SqlLiteralDate(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toSimpleSql() {
        return "DATE '" + this.value + "'";
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
