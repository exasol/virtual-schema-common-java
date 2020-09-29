package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

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

    public List<SqlNode> getArguments() {
        if (arguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(arguments);
        }
    }

    public AggregateFunction getFunction() {
        return function;
    }

    public String getFunctionName() {
        return function.toString();
    }

    public boolean hasDistinct() {
        return distinct;
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