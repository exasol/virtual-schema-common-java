package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.Predicate;

/**
 * List of all Predicates (scalar functions returning bool) supported by EXASOL.
 */
public enum PredicateCapability {
    /**
     * And predicate capability.
     */
    AND,
    /**
     * Or predicate capability.
     */
    OR,
    /**
     * Not predicate capability.
     */
    NOT,
    /**
     * Equal predicate capability.
     */
    EQUAL,
    /**
     * Notequal predicate capability.
     */
    NOTEQUAL,
    /**
     * Less predicate capability.
     */
    LESS,
    /**
     * Lessequal predicate capability.
     */
    LESSEQUAL,
    /**
     * Like predicate capability.
     */
    LIKE,
    /**
     * The LIKE predicate with the optional escape character defined
     */
    LIKE_ESCAPE(Predicate.LIKE),
    /**
     * Regexp like predicate capability.
     */
    REGEXP_LIKE,
    /**
     * Between predicate capability.
     */
    BETWEEN,
    /**
     * In constlist predicate capability.
     */
    IN_CONSTLIST,
    /**
     * Is null predicate capability.
     */
    IS_NULL,
    /**
     * Is not null predicate capability.
     */
    IS_NOT_NULL,
    /**
     * Is json predicate capability.
     */
    IS_JSON,
    /**
     * Is not json predicate capability.
     */
    IS_NOT_JSON;

    private final Predicate predicate;

    PredicateCapability() {
        this.predicate = Predicate.valueOf(name());
    }

    PredicateCapability(final Predicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Gets predicate.
     *
     * @return the predicate
     */
    public Predicate getPredicate() {
        return this.predicate;
    }
}
