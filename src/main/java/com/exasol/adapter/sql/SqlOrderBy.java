package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

public class SqlOrderBy extends SqlNode {
    private final List<SqlNode> expressions;
    private final List<Boolean> isAsc;
    private final List<Boolean> nullsLast;

    public SqlOrderBy(final List<SqlNode> expressions, final List<Boolean> isAsc, final List<Boolean> nullsFirst) {
        this.expressions = expressions;
        this.isAsc = isAsc;
        this.nullsLast = nullsFirst;
        if (this.expressions != null) {
            for (final SqlNode node : this.expressions) {
                node.setParent(this);
            }
        }
    }

    public List<SqlNode> getExpressions() {
        if (this.expressions == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.expressions);
        }
    }

    public List<Boolean> isAscending() {
        if (this.isAsc == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.isAsc);
        }
    }

    public List<Boolean> nullsLast() {
        if (this.nullsLast == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.nullsLast);
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.ORDER_BY;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}