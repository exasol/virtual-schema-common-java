package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralTimestampUtc extends SqlNode {
    //Format: YYYY-MM-DD HH:MI:SS.FF6
    private final String value;
    
    public SqlLiteralTimestampUtc(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toSimpleSql() {
        return "TIMESTAMP '" + this.value + "'";
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
