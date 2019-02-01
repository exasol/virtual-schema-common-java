package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

import java.util.Collections;
import java.util.List;

public class SqlFunctionScalarCast extends SqlNode {
    private DataType dataType;
    private List<SqlNode> arguments;

    public SqlFunctionScalarCast(DataType dataType, List<SqlNode> arguments) {
        if (arguments == null) {
            throw new IllegalArgumentException("SqlFunctionScalarCast constructor expects an argument." +
                  "But the list is empty.");
        }
        if (arguments.size() != 1){
            throw new IllegalArgumentException("SqlFunctionScalarCast constructor expects exactly one argument." +
                  "But got " + arguments.size() + "arguments.");
        }
        if (arguments.get(0) == null){
            throw new IllegalArgumentException("SqlFunctionScalarCast constructor expects an argument." +
                  "But the list is empty.");
        }
        this.arguments = arguments;
        this.dataType = dataType;
        for (SqlNode node : this.arguments) {
            node.setParent(this);
        }
    }

    public DataType getDataType() {
        return dataType;
    }


    public List<SqlNode> getArguments() {
        if (arguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(arguments);
        }
    }

    @Override
    public String toSimpleSql() {
        assert(arguments != null);
        assert(arguments.size() == 1 && arguments.get(0) != null);
        return "CAST (" + arguments.get(0).toSimpleSql() + " AS " + getDataType().toString() + ")";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_CAST;
    }

    @Override
    public <R> R accept(SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    public ScalarFunction getFunction() {
        return ScalarFunction.CAST;
    }

}
