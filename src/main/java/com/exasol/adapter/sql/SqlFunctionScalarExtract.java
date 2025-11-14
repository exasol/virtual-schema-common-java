package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.List;

/**
 * The type Sql function scalar extract.
 */
public class SqlFunctionScalarExtract extends SqlNode {
    private final ExtractParameter extractType;
    private final SqlNode argument;

    /**
     * Instantiates a new Sql function scalar extract.
     *
     * @param extractParameter the extract parameter
     * @param argument         the argument
     */
    public SqlFunctionScalarExtract(final ExtractParameter extractParameter, final SqlNode argument) {
        this.argument = argument;
        this.extractType = extractParameter;
        argument.setParent(this);
    }

    /**
     * Gets to extract.
     *
     * @return the to extract
     */
    public String getToExtract() {
        return this.extractType.toString();
    }

    /**
     * Gets argument.
     *
     * @return the argument
     */
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

    @Override
    public List<SqlNode> getChildren() { return List.of(this.argument); }

    /**
     * List of available parameters for the EXTRACT function.
     */
    public enum ExtractParameter {
        /**
         * Year extract parameter.
         */
        YEAR,
        /**
         * Month extract parameter.
         */
        MONTH,
        /**
         * Day extract parameter.
         */
        DAY,
        /**
         * Hour extract parameter.
         */
        HOUR,
        /**
         * Minute extract parameter.
         */
        MINUTE,
        /**
         * Second extract parameter.
         */
        SECOND;
    }
}