package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

/**
 * The type Sql function scalar cast.
 */
public class SqlFunctionScalarCast extends SqlNode {
    private final DataType dataType;
    private final SqlNode argument;

    /**
     * Create a new instance of {@link SqlFunctionScalarCast}.
     *
     * @param dataType cast data type
     * @param argument value to cast
     */
    public SqlFunctionScalarCast(final DataType dataType, final SqlNode argument) {
        this.argument = argument;
        this.dataType = dataType;
        argument.setParent(this);
    }

    /**
     * Gets data type.
     *
     * @return the data type
     */
    public DataType getDataType() {
        return this.dataType;
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
        return SqlNodeType.FUNCTION_SCALAR_CAST;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}