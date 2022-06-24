package com.exasol.adapter.sql;

/**
 * This class represents SQL predicates.
 */
public abstract class SqlPredicate extends SqlNode {
    private final Predicate function;

    /**
     * Instantiates a new Sql predicate.
     *
     * @param function the function
     */
    public SqlPredicate(final Predicate function) {
        this.function = function;
    }

    /**
     * Gets function.
     *
     * @return the function
     */
    public Predicate getFunction() {
        return this.function;
    }
}
