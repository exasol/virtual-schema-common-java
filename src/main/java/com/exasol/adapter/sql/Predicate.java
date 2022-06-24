package com.exasol.adapter.sql;

/**
 * List of all predicates (scalar functions returning bool) supported by EXASOL.
 */
public enum Predicate {
    /**
     * And predicate.
     */
    AND,
    /**
     * Or predicate.
     */
    OR,
    /**
     * Not predicate.
     */
    NOT,
    /**
     * Equal predicate.
     */
    EQUAL,
    /**
     * Notequal predicate.
     */
    NOTEQUAL,
    /**
     * Less predicate.
     */
    LESS,
    /**
     * Lessequal predicate.
     */
    LESSEQUAL,
    /**
     * Like predicate.
     */
    LIKE,
    /**
     * Regexp like predicate.
     */
    REGEXP_LIKE,
    /**
     * Between predicate.
     */
    BETWEEN,
    /**
     * In constlist predicate.
     */
    IN_CONSTLIST,
    /**
     * Is null predicate.
     */
    IS_NULL,
    /**
     * Is not null predicate.
     */
    IS_NOT_NULL,
    /**
     * Is json predicate.
     */
    IS_JSON,
    /**
     * Is not json predicate.
     */
    IS_NOT_JSON
}
