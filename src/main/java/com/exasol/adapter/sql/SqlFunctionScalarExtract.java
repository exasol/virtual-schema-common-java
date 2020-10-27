package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

public class SqlFunctionScalarExtract extends SqlNode {
    private final ExtractParameter extractType;
    private final SqlNode argument;

    public SqlFunctionScalarExtract(final ExtractParameter extractParameter, final SqlNode argument) {
        this.argument = argument;
        this.extractType = extractParameter;
        argument.setParent(this);
    }

    public String getToExtract() {
        return this.extractType.toString();
    }

    public SqlNode getArgument() {
        return this.argument;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_EXTRACT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    /**
     * List of available parameters for the EXTRACT function.
     */
    public enum ExtractParameter {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND;
    }
}