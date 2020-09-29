package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

public class SqlPredicateOr extends SqlPredicate {
    private final List<SqlNode> orPredicates;

    public SqlPredicateOr(final List<SqlNode> orPredicates) {
        super(Predicate.OR);
        this.orPredicates = orPredicates;
        if (this.orPredicates != null) {
            for (final SqlNode node : this.orPredicates) {
                node.setParent(this);
            }
        }
    }

    public List<SqlNode> getOrPredicates() {
        if (orPredicates == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(orPredicates);
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_OR;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}