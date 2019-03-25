package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonReader;

import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.sql.SqlStatement;

/**
 * Parser for JSON structures representing a Virtual Schema Adapter request.
 */
public class RequestParser extends AbstractRequestParser {
    /**
     * Parse a JSON string containing a Virtual Schema Adapter request into the abstract representation of that request
     *
     * @param rawRequest request as JSON string
     * @return parsed request
     * @throws RequestParserException if an unknown request type is encountered
     */
    public AbstractAdapterRequest parse(final String rawRequest) {
        try (final JsonReader reader = createJsonReader(rawRequest)) {
            return parseFromReader(reader);
        }
    }

    private AbstractAdapterRequest parseFromReader(final JsonReader reader) {
        final JsonObject root = reader.readObject();
        final String type = readRequestType(root);
        final SchemaMetadataInfo metadataInfo = readSchemaMetadataInfo(root);
        final String adapterName = metadataInfo.getProperty(ADPTER_NAME_PROPERTY_KEY);
        final Map<String, String> adapterProperties = parseProperties(root);
        switch (type) {
        case REQUEST_TYPE_DROP_VIRTUAL_SCHEMA:
            return new DropVirtualSchemaRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_CREATE_VIRTUAL_SCHEMA:
            return new CreateVirtualSchemaRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_REFRESH:
            return new RefreshRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_SET_PROPERTIES:
            return new SetPropertiesRequest(adapterName, metadataInfo, adapterProperties);
        case REQUEST_TYPE_GET_CAPABILITIES:
            return new GetCapabilitiesRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_PUSHDOWN:
            final SqlStatement statement = parsePushdownStatement(root);
            return new PushDownRequest(adapterName, metadataInfo, statement, null);
        default:
            throw new RequestParserException("Unknown request type \"" + type + "\"");
        }
    }

    private String readRequestType(final JsonObject root) {
        return root.getString(ADAPTER_REQUEST_TYPE_KEY);
    }

    private SchemaMetadataInfo readSchemaMetadataInfo(final JsonObject root) {
        JsonObject schemaMetadataInfoAsJson = root.getJsonObject(SCHEMA_METADATA_INFO_KEY);
        return new SchemaMetadataInfoParser().parse(schemaMetadataInfoAsJson);
    }

    private SqlStatement parsePushdownStatement(final JsonObject root) {
        final List<TableMetadata> involvedTables = parseInvolvedTables(root);
        final PushdownSqlParser pushdownSqlParser = PushdownSqlParser.createWithTablesMetadata(involvedTables);
        final JsonObject jsonPushdownStatement = root.getJsonObject(PUSHDOW_REQUEST_KEY);
        return (SqlStatement) pushdownSqlParser.parseExpression(jsonPushdownStatement);
    }

    private List<TableMetadata> parseInvolvedTables(final JsonObject root) {
        return TablesMetadataParser.create().parse(root.getJsonArray(INVOLVED_TABLES_KEY));
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