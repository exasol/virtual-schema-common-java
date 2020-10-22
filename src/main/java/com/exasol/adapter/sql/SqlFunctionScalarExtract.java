package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

public class SqlFunctionScalarExtract extends SqlNode {
    private final ExtractParameter extractType;
    private final List<SqlNode> arguments;

    public SqlFunctionScalarExtract(final ExtractParameter extractParameter, final List<SqlNode> arguments) {
        SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionScalarExtract.class);
        this.arguments = arguments;
        this.extractType = extractParameter;
        for (final SqlNode node : this.arguments) {
            node.setParent(this);
        }
    }

    public String getToExtract() {
        return this.extractType.toString();
    }

    public List<SqlNode> getArguments() {
        return Collections.unmodifiableList(this.arguments);
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