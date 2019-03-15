package com.exasol.adapter.request.parser;

final class RequestParserConstants {
    public static final String REQUEST_TYPE_CREATE_VIRTUAL_SCHEMA = "createVirtualSchema";
    public static final String REQUEST_TYPE_DROP_VIRTUAL_SCHEMA = "dropVirtualSchema";
    public static final String REQUEST_TYPE_REFRESH = "refresh";
    public static final String REQUEST_TYPE_SET_PROPERTIES = "setProperties";
    public static final String REQUEST_TYPE_GET_CAPABILITIES = "getCapabilities";
    public static final String REQUEST_TYPE_PUSHDOWN = "pushdown";
    public static final String ADAPTER_REQUEST_TYPE_KEY = "type";
    public static final String ADAPTER_NOTES_KEY = "adapterNotes";
    public static final String PROPERTIES_KEY = "properties";
    public static final String SCHEMA_NAME_KEY = "name";
    public static final String PUSHDOW_REQUEST_KEY = "pushdownRequest";
    public static final String SCHEMA_METADATA_INFO_KEY = "schemaMetadataInfo";

    private RequestParserConstants() {
        // prevent instantiation
    }
}