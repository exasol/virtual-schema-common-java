package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

/**
 * Interval literal.
 */
public class SqlLiteralInterval extends SqlNode {
    private final String value;
    private final DataType type;

    /**
     * Instantiates a new Sql literal interval.
     *
     * @param value the value
     * @param type  the data type
     */
    public SqlLiteralInterval(final String value, final DataType type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Get the value.
     * 
     * @return literal interval value in format: YYYY-MM-DD HH:MI:SS.FF6
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets data type.
     *
     * @return the data type
     */
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