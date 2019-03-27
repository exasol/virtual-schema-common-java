package com.exasol.adapter.request;

/**
 * Type of a request to a Virtual Schema Adapter
 */
public enum AdapterRequestType {
    CREATE_VIRTUAL_SCHEMA, DROP_VIRTUAL_SCHEMA, REFRESH, SET_PROPERTIES, GET_CAPABILITIES, PUSHDOWN
}