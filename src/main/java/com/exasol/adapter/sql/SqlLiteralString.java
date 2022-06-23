package com.exasol.adapter.sql;

import java.util.Objects;

import com.exasol.adapter.AdapterException;

/**
 * String literal.
 */
public class SqlLiteralString extends SqlNode {
    private final String value;

    /**
     * Instantiates a new Sql literal string.
     *
     * @param value the value
     */
    public SqlLiteralString(final String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_STRING;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SqlLiteralString)) {
            return false;
        }
        final SqlLiteralString that = (SqlLiteralString) object;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}