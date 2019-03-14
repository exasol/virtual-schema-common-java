package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import javax.json.JsonObject;
import javax.json.JsonReader;

import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.adapter.request.*;

/**
 * Parser for JSON structures representing a Virtual Schema Adapter request.
 */
public class RequestParser extends AbstractRequestParser {

    /**
     * Parse a JSON string containing a Virtual Schema Adapter request into the
     * abstract representation of that request
     *
     * @param rawRequest request as JSON string
     * @return parsed request
     * @throws RequestParserException if an unknown request type is encountered
     */
    public AdapterRequest parse(final String rawRequest) {
        final JsonReader reader = createJsonReader(rawRequest);
        final JsonObject root = reader.readObject();
        final String type = readRequestType(root);
        final SchemaMetadataInfo metadataInfo = readSchemaMetadataInfo(root);
        switch (type) {
        case DROP_VIRTUAL_SCHEMA:
            return new DropVirtualSchemaRequest(metadataInfo);
        case CREATE_VIRTUAL_SCHEMA:
            return new CreateVirtualSchemaRequest(metadataInfo);
        default:
            throw new RequestParserException("Unknown request type \"" + type + "\"");
        }
    }

    private String readRequestType(final JsonObject root) {
        final String type = root.getString(ADAPTER_REQUEST_TYPE_KEY);
        return type;
    }

    private SchemaMetadataInfo readSchemaMetadataInfo(final JsonObject root) {
        final SchemaMetadataInfo metadataInfo = new SchemaMetadataInfoParser()
                .parse(root.getJsonObject("schemaMetadataInfo"));
        return metadataInfo;
    }

    /**
     * Create a {@link RequestParser}
     * 
     * @return request parser instance
     */
    public static RequestParser create() {
        return new RequestParser();
    }
}