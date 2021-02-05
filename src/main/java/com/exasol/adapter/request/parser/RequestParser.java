package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.json.*;

import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.sql.SqlStatement;
import com.exasol.errorreporting.ExaError;

/**
 * Parser for JSON structures representing a Virtual Schema Adapter request.
 */
public class RequestParser extends AbstractRequestParser {
    private static final Logger LOGGER = Logger.getLogger(RequestParser.class.getName());

    /**
     * Parse a JSON string containing a Virtual Schema Adapter request into the abstract representation of that request
     *
     * @param rawRequest request as JSON string
     * @return parsed request
     * @throws RequestParserException if an unknown request type is encountered
     */
    public AdapterRequest parse(final String rawRequest) {
        try (final JsonReader reader = createJsonReader(rawRequest)) {
            return parseFromReader(reader);
        }
    }

    private AbstractAdapterRequest parseFromReader(final JsonReader reader) {
        final JsonObject root = reader.readObject();
        final String type = readRequestType(root);
        final SchemaMetadataInfo metadataInfo = readSchemaMetadataInfo(root);
        final Map<String, String> adapterProperties = parseProperties(root);
        switch (type) {
        case REQUEST_TYPE_DROP_VIRTUAL_SCHEMA:
            return new DropVirtualSchemaRequest(metadataInfo);
        case REQUEST_TYPE_CREATE_VIRTUAL_SCHEMA:
            return new CreateVirtualSchemaRequest(metadataInfo);
        case REQUEST_TYPE_REFRESH:
            return parseRefreshRequest(root, metadataInfo);
        case REQUEST_TYPE_SET_PROPERTIES:
            return new SetPropertiesRequest(metadataInfo, adapterProperties);
        case REQUEST_TYPE_GET_CAPABILITIES:
            return new GetCapabilitiesRequest(metadataInfo);
        case REQUEST_TYPE_PUSHDOWN:
            return parsePushdownRequest(root, metadataInfo);
        default:
            throw new RequestParserException(ExaError.messageBuilder("E-VS-COM-JAVA-16")
                    .message("Could not parse unknown adapter request type identifier {{type}}.")
                    .mitigation("Check whether versions of Exasol database and Virtual Schema Adapter are compatible.")
                    .parameter("type", type).toString());
        }
    }

    private AbstractAdapterRequest parseRefreshRequest(final JsonObject root, final SchemaMetadataInfo metadataInfo) {
        if (root.containsKey(REFRESH_TABLES_KEY)) {
            final List<String> tables = root.getJsonArray(REFRESH_TABLES_KEY) //
                    .stream() //
                    .map((table -> ((JsonString) table).getString())) //
                    .collect(Collectors.toList());
            return new RefreshRequest(metadataInfo, tables);
        } else {
            return new RefreshRequest(metadataInfo);
        }
    }

    private AbstractAdapterRequest parsePushdownRequest(final JsonObject root, final SchemaMetadataInfo metadataInfo) {
        final SqlStatement statement = parsePushdownStatement(root);
        final List<TableMetadata> involvedTables = parseInvolvedTables(root);
        return new PushDownRequest(metadataInfo, statement, involvedTables);
    }

    private List<TableMetadata> parseInvolvedTables(final JsonObject root) {
        return TablesMetadataParser.create().parse(root.getJsonArray(INVOLVED_TABLES_KEY));
    }

    private String readRequestType(final JsonObject root) {
        return root.getString(ADAPTER_REQUEST_TYPE_KEY);
    }

    private SchemaMetadataInfo readSchemaMetadataInfo(final JsonObject root) {
        if (root.containsKey(SCHEMA_METADATA_INFO_KEY)) {
            final JsonObject schemaMetadataInfoAsJson = root.getJsonObject(SCHEMA_METADATA_INFO_KEY);
            return new SchemaMetadataInfoParser().parse(schemaMetadataInfoAsJson);
        } else {
            LOGGER.severe("Missing metadata information trying to parse adapter request.");
            return new SchemaMetadataInfo("UNKNOWN", "", new HashMap<>());
        }
    }

    private SqlStatement parsePushdownStatement(final JsonObject root) {
        final List<TableMetadata> involvedTables = parseInvolvedTables(root);
        final PushdownSqlParser pushdownSqlParser = PushdownSqlParser.createWithTablesMetadata(involvedTables);
        final JsonObject jsonPushdownStatement = root.getJsonObject(PUSHDOW_REQUEST_KEY);
        return (SqlStatement) pushdownSqlParser.parseExpression(jsonPushdownStatement);
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