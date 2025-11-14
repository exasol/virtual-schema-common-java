package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

/**
 * SQL expression list.
 */
public abstract class SqlExpressionList extends SqlNode {
    private final List<SqlNode> expressions;

    /**
     * Instantiates a new Sql expression list.
     *
     * @param expressions the expressions
     */
    public SqlExpressionList(final List<SqlNode> expressions) {
        this.expressions = expressions;
        if (this.expressions != null) {
            for (final SqlNode node : this.expressions) {
                node.setParent(this);
            }
        }
    }

    /**
     * Gets expressions.
     *
     * @return the expressions
     */
    public List<SqlNode> getExpressions() {
        if (this.expressions == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.expressions);
        }
    }

    @Override
    public List<SqlNode> getChildren() { return getExpressions(); }
}
