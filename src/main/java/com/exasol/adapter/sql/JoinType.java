package com.exasol.adapter.sql;

/**
 * List of all join types supported by EXASOL.
 */
public enum JoinType {
    /**
     * Inner join type.
     */
    INNER,
    /**
     * Left outer join type.
     */
    LEFT_OUTER,
    /**
     * Right outer join type.
     */
    RIGHT_OUTER,
    /**
     * Full outer join type.
     */
    FULL_OUTER
}