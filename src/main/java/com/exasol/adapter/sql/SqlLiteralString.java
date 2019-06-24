package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlLiteralString extends SqlNode {
    private final String value;

    public SqlLiteralString(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toSimpleSql() {
        return "'" + this.value.replace("'", "''") + "'";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_STRING;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
