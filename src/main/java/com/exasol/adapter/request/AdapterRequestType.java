package com.exasol.adapter.request;

/**
 * Type of a request to a Virtual Schema Adapter
 */
public enum AdapterRequestType {
    /**
     * Create virtual schema adapter request type.
     */
    CREATE_VIRTUAL_SCHEMA,
    /**
     * Drop virtual schema adapter request type.
     */
    DROP_VIRTUAL_SCHEMA,
    /**
     * Refresh adapter request type.
     */
    REFRESH,
    /**
     * Set properties adapter request type.
     */
    SET_PROPERTIES,
    /**
     * Get capabilities adapter request type.
     */
    GET_CAPABILITIES,
    /**
     * Pushdown adapter request type.
     */
    PUSHDOWN
}