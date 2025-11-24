package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * {@code OR} predicate.
 */
public class SqlPredicateOr extends SqlPredicate {
    private final List<SqlNode> orPredicates;

    /**
     * Instantiates a new Sql predicate or.
     *
     * @param orPredicates the or predicates
     */
    public SqlPredicateOr(final List<SqlNode> orPredicates) {
        super(Predicate.OR);
        this.orPredicates = orPredicates;
        if (this.orPredicates != null) {
            for (final SqlNode node : this.orPredicates) {
                node.setParent(this);
            }
        }
    }

    /**
     * Gets or predicates.
     *
     * @return the or predicates
     */
    public List<SqlNode> getOrPredicates() {
        if (this.orPredicates == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.orPredicates);
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

    @Override
    public List<SqlNode> getChildren() { return getOrPredicates(); }
}