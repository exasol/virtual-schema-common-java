package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

public class SqlLiteralInterval extends SqlNode {
    private final String value;
    private final DataType type;

    public SqlLiteralInterval(final String value, final DataType type) {
        this.value = value;
        this.type = type;
    }

    /**
     * @return literal interval value in format: YYYY-MM-DD HH:MI:SS.FF6
     */
    public String getValue() {
        return this.value;
    }

    public DataType getDataType() {
        return this.type;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_INTERVAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}