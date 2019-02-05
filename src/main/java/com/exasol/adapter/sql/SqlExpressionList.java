package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

public abstract class SqlExpressionList extends SqlNode {
    private final List<SqlNode> expressions;

    public SqlExpressionList(final List<SqlNode> expressions) {
        this.expressions = expressions;
        if (this.expressions != null) {
            for (final SqlNode node : this.expressions) {
                node.setParent(this);
            }
        }
    }

    public List<SqlNode> getExpressions() {
        if (expressions == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(expressions);
        }
    }
}
