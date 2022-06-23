package com.exasol.adapter.sql;

import java.util.Collections;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * {@code AND} predicate.
 */
public class SqlPredicateAnd extends SqlPredicate {
    private final List<SqlNode> andedPredicates;

    /**
     * Instantiates a new Sql predicate and.
     *
     * @param andedPredicates the anded predicates
     */
    public SqlPredicateAnd(final List<SqlNode> andedPredicates) {
        super(Predicate.AND);
        this.andedPredicates = andedPredicates;
        if (this.andedPredicates != null) {
            for (final SqlNode node : this.andedPredicates) {
                node.setParent(this);
            }
        }
    }

    /**
     * Gets anded predicates.
     *
     * @return the anded predicates
     */
    public List<SqlNode> getAndedPredicates() {
        if (this.andedPredicates == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.andedPredicates);
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_AND;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}