package com.exasol.adapter.request.parser;

final class RequestParserConstants {
    public static final String CREATE_VIRTUAL_SCHEMA = "createVirtualSchema";
    public static final String DROP_VIRTUAL_SCHEMA = "dropVirtualSchema";
    public static final String ADAPTER_REQUEST_TYPE_KEY = "type";
    public static final String ADAPTER_NOTES_KEY = "adapterNotes";
    public static final String PROPERTIES_KEY = "properties";
    public static final String SCHEMA_NAME_KEY = "name";

    private RequestParserConstants() {
        // prevent instantiation
    }
}
