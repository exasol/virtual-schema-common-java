package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

import java.util.Collections;
import java.util.List;

public class SqlFunctionScalarCast extends SqlNode {
    private final DataType dataType;
    private final List<SqlNode> arguments;

    public SqlFunctionScalarCast(final DataType dataType, final List<SqlNode> arguments) {
        SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionScalarCast.class);
        this.arguments = arguments;
        this.dataType = dataType;
        for (final SqlNode node : this.arguments) {
            node.setParent(this);
        }
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public List<SqlNode> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    @Override
    public String toSimpleSql() {
        return "CAST";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_CAST;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}