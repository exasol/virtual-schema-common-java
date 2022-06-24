package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * A simple aggregate function with a name and zero or more arguments. Distinct is also allowed.
 *
 * <p>
 * Aggregate functions that are more complex, like GroupConcat, are defined in separate classes.
 * </p>
 */
public class SqlFunctionAggregate extends SqlNode {
    private final AggregateFunction function;
    private final boolean distinct;
    private final List<SqlNode> arguments;

    /**
     * Instantiates a new Sql function aggregate.
     *
     * @param function  the function
     * @param arguments the arguments
     * @param distinct  the distinct
     */
    public SqlFunctionAggregate(final AggregateFunction function, final List<SqlNode> arguments,
            final boolean distinct) {
        this.arguments = arguments;
        this.function = function;
        this.distinct = distinct;
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
    public AggregateFunction getFunction() {
        return this.function;
    }

    /**
     * Gets function name.
     *
     * @return the function name
     */
    public String getFunctionName() {
        return this.function.toString();
    }

    /**
     * Has distinct boolean.
     *
     * @return the boolean
     */
    public boolean hasDistinct() {
        return this.distinct;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_AGGREGATE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}