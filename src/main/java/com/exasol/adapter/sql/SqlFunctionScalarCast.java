package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

public class SqlFunctionScalarCast extends SqlNode {
    private final DataType dataType;
    private final SqlNode argument;

    public SqlFunctionScalarCast(final DataType dataType, final SqlNode argument) {
        this.argument = argument;
        this.dataType = dataType;
        argument.setParent(this);
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public SqlNode getArgument() {
        return this.argument;
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