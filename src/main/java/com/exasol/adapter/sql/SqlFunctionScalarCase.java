package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

public class SqlFunctionScalarCase extends SqlNode {
    private final List<SqlNode> arguments;
    private final List<SqlNode> results;
    private final SqlNode basis;

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

    public List<SqlNode> getArguments() {
        if (arguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(arguments);
        }
    }

    public List<SqlNode> getResults() {
        if (results == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(results);
        }
    }

    public SqlNode getBasis() {
        return basis;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.FUNCTION_SCALAR_CASE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}