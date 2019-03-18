package com.exasol.adapter.request.parser;

final class RequestParserConstants {
    static final String REQUEST_TYPE_CREATE_VIRTUAL_SCHEMA = "createVirtualSchema";
    static final String REQUEST_TYPE_DROP_VIRTUAL_SCHEMA = "dropVirtualSchema";
    static final String REQUEST_TYPE_REFRESH = "refresh";
    static final String REQUEST_TYPE_SET_PROPERTIES = "setProperties";
    static final String REQUEST_TYPE_GET_CAPABILITIES = "getCapabilities";
    static final String REQUEST_TYPE_PUSHDOWN = "pushdown";
    static final String ADAPTER_REQUEST_TYPE_KEY = "type";
    static final String ADAPTER_NOTES_KEY = "adapterNotes";
    static final String PROPERTIES_KEY = "properties";
    static final String SCHEMA_NAME_KEY = "name";
    static final String PUSHDOW_REQUEST_KEY = "pushdownRequest";
    static final String SCHEMA_METADATA_INFO_KEY = "schemaMetadataInfo";
    static final String ADPTER_NAME_PROPERTY_KEY = "SQL_DIALECT";

    private RequestParserConstants() {
        // prevent instantiation
    }
}