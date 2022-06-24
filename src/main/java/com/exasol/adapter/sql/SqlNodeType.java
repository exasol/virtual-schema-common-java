package com.exasol.adapter.sql;

/**
 * All types of nodes that can be part of a pushdown request. Each type represents a different class inheriting from
 * SqlNode.
 */
public enum SqlNodeType {
    /**
     * Select sql node type.
     */
    SELECT,
    /**
     * Table sql node type.
     */
    TABLE,
    /**
     * Join sql node type.
     */
    JOIN,
    /**
     * Select list sql node type.
     */
    SELECT_LIST,
    /**
     * Group by sql node type.
     */
    GROUP_BY,
    /**
     * Column sql node type.
     */
    COLUMN,
    /**
     * Literal null sql node type.
     */
    LITERAL_NULL,
    /**
     * Literal bool sql node type.
     */
    LITERAL_BOOL,
    /**
     * Literal date sql node type.
     */
    LITERAL_DATE,
    /**
     * Literal timestamp sql node type.
     */
    LITERAL_TIMESTAMP,
    /**
     * Literal timestamputc sql node type.
     */
    LITERAL_TIMESTAMPUTC,
    /**
     * Literal double sql node type.
     */
    LITERAL_DOUBLE,
    /**
     * Literal exactnumeric sql node type.
     */
    LITERAL_EXACTNUMERIC,
    /**
     * Literal string sql node type.
     */
    LITERAL_STRING,
    /**
     * Literal interval sql node type.
     */
    LITERAL_INTERVAL,
    /**
     * Predicate and sql node type.
     */
    PREDICATE_AND,
    /**
     * Predicate or sql node type.
     */
    PREDICATE_OR,
    /**
     * Predicate not sql node type.
     */
    PREDICATE_NOT,
    /**
     * Predicate equal sql node type.
     */
    PREDICATE_EQUAL,
    /**
     * Predicate notequal sql node type.
     */
    PREDICATE_NOTEQUAL,
    /**
     * Predicate less sql node type.
     */
    PREDICATE_LESS,
    /**
     * Predicate lessequal sql node type.
     */
    PREDICATE_LESSEQUAL,
    /**
     * Predicate like sql node type.
     */
    PREDICATE_LIKE,
    /**
     * Predicate like regexp sql node type.
     */
    PREDICATE_LIKE_REGEXP,
    /**
     * Predicate between sql node type.
     */
    PREDICATE_BETWEEN,
    /**
     * Predicate in constlist sql node type.
     */
    PREDICATE_IN_CONSTLIST,
    /**
     * Predicate is null sql node type.
     */
    PREDICATE_IS_NULL,
    /**
     * Predicate is not null sql node type.
     */
    PREDICATE_IS_NOT_NULL,
    /**
     * Predicate is json sql node type.
     */
    PREDICATE_IS_JSON,
    /**
     * Predicate is not json sql node type.
     */
    PREDICATE_IS_NOT_JSON,
    /**
     * Function scalar sql node type.
     */
    FUNCTION_SCALAR,
    /**
     * Function scalar case sql node type.
     */
    FUNCTION_SCALAR_CASE,
    /**
     * Function scalar cast sql node type.
     */
    FUNCTION_SCALAR_CAST,
    /**
     * Function scalar extract sql node type.
     */
    FUNCTION_SCALAR_EXTRACT,
    /**
     * Function scalar json value sql node type.
     */
    FUNCTION_SCALAR_JSON_VALUE,
    /**
     * Function aggregate sql node type.
     */
    FUNCTION_AGGREGATE,
    /**
     * Function aggregate group concat sql node type.
     */
    FUNCTION_AGGREGATE_GROUP_CONCAT,
    /**
     * Function aggregate listagg sql node type.
     */
    FUNCTION_AGGREGATE_LISTAGG,
    /**
     * Order by sql node type.
     */
    ORDER_BY,
    /**
     * Limit sql node type.
     */
    LIMIT
}