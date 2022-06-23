package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * A simple scalar function with a name and zero or more arguments.
 *
 * <p>
 * Scalar functions that are more complex, like CASE or CAST, are defined in separate classes.
 * </p>
 */
public class SqlFunctionScalar extends SqlNode {
    private final List<SqlNode> arguments;
    private final ScalarFunction function;

    /**
     * Instantiates a new Sql function scalar.
     *
     * @param function  the function
     * @param arguments the arguments
     */
    public SqlFunctionScalar(final ScalarFunction function, final List<SqlNode> arguments) {
        this.arguments = arguments;
        this.function = function;
        if (this.arguments != null) {
            for (final SqlNode node : this.arguments) {
                node.setParent(this);
            }
        }
    }

    /**
     * Gets arguments.
     *
     * @return the arguments
     */
    public List<SqlNode> getArguments() {
        if (this.arguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.arguments);
        }
    }

    /**
     * Gets function.
     *
     * @return the function
     */
    public ScalarFunction getFunction() {
        return this.function;
    }

    /**
     * Gets function name.
     *
     * @return the function name
     */
    public String getFunctionName() {
        return this.function.name();
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}