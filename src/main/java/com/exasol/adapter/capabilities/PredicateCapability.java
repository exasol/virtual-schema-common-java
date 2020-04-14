package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.*;

/**
 * List of all Predicates (scalar functions returning bool) supported by EXASOL.
 */
public enum PredicateCapability {
    AND, OR, NOT, EQUAL, NOTEQUAL, LESS, LESSEQUAL, LIKE,
    /**
     * The LIKE predicate with the optional escape character defined
     */
    LIKE_ESCAPE(Predicate.LIKE), REGEXP_LIKE, BETWEEN, IN_CONSTLIST, IS_NULL, IS_NOT_NULL,
    IS_JSON, IS_NOT_JSON;

    private final Predicate predicate;

    PredicateCapability() {
        this.predicate = Predicate.valueOf(name());
    }

    PredicateCapability(final Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return this.predicate;
    }
}
