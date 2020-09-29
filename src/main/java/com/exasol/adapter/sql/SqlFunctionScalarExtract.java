package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

public class SqlFunctionScalarExtract extends SqlNode {
    private final String toExtract;
    private final List<SqlNode> arguments;

    public SqlFunctionScalarExtract(final String toExtract, final List<SqlNode> arguments) {
        SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionScalarExtract.class);
        this.arguments = arguments;
        this.toExtract = toExtract;
        for (final SqlNode node : this.arguments) {
            node.setParent(this);
        }
    }

    public String getToExtract() {
        return this.toExtract;
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
}