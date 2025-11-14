package com.exasol.adapter.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * {@code CASE} function.
 */
public class SqlFunctionScalarCase extends SqlNode {
    private final List<SqlNode> arguments;
    private final List<SqlNode> results;
    private final SqlNode basis;

    /**
     * Instantiates a new SQL function scalar case.
     *
     * @param arguments the arguments
     * @param results   the results
     * @param basis     the basis
     */
    public SqlFunctionScalarCase(final List<SqlNode> arguments, final List<SqlNode> results, final SqlNode basis) {
        this.arguments = arguments;
        this.results = results;
        this.basis = basis;
        if (this.arguments != null) {
            for (final SqlNode node : this.arguments) {
                node.setParent(this);
            }
        }
        if (this.results != null) {
            for (final SqlNode node : this.results) {
                node.setParent(this);
            }
        }
        if (basis != null) {
            basis.setParent(this);
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
     * Gets results.
     *
     * @return the results
     */
    public List<SqlNode> getResults() {
        if (this.results == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.results);
        }
    }

    /**
     * Gets basis.
     *
     * @return the basis
     */
    public SqlNode getBasis() {
        return this.basis;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_CASE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() {
        ArrayList<SqlNode> children = new ArrayList<>(getArguments());
        children.addAll(getResults());
        return children;
    }
}