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
        final String adapterName = extractAdapterNameFromMetadataInfo(metadataInfo);
        final Map<String, String> adapterProperties = parseProperties(root);
        switch (type) {
        case REQUEST_TYPE_DROP_VIRTUAL_SCHEMA:
            return new DropVirtualSchemaRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_CREATE_VIRTUAL_SCHEMA:
            return new CreateVirtualSchemaRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_REFRESH:
            return parseRefreshRequest(root, metadataInfo, adapterName);
        case REQUEST_TYPE_SET_PROPERTIES:
            return new SetPropertiesRequest(adapterName, metadataInfo, adapterProperties);
        case REQUEST_TYPE_GET_CAPABILITIES:
            return new GetCapabilitiesRequest(adapterName, metadataInfo);
        case REQUEST_TYPE_PUSHDOWN:
            return parsePushdownRequest(root, metadataInfo, adapterName);
        default:
            throw new RequestParserException("Could not parse unknown adapter request type identifier \"" + type
                    + "\". Check whether versions of Exasol database and Virtual Schema Adapter are compatible.");
        }
    }

    private AbstractAdapterRequest parseRefreshRequest(final JsonObject root, final SchemaMetadataInfo metadataInfo,
            final String adapterName) {
        if (root.containsKey(REFRESH_TABLES_KEY)) {
            final List<String> tables = root.getJsonArray(REFRESH_TABLES_KEY) //
                    .stream() //
                    .map((table -> ((JsonString) table).getString())) //
                    .collect(Collectors.toList());
            return new RefreshRequest(adapterName, metadataInfo, tables);
        } else {
            return new RefreshRequest(adapterName, metadataInfo);
        }
    }

    private AbstractAdapterRequest parsePushdownRequest(final JsonObject root, final SchemaMetadataInfo metadataInfo,
            final String adapterName) {
        final SqlStatement statement = parsePushdownStatement(root);
        final List<TableMetadata> involvedTables = parseInvolvedTablesMetadata(root);
        return new PushDownRequest(adapterName, metadataInfo, statement, involvedTables);
    }

    private List<TableMetadata> parseInvolvedTablesMetadata(final JsonObject root) {
        return new TablesMetadataParser().parse(root.getJsonArray("involvedTables"));
    }

    private String extractAdapterNameFromMetadataInfo(final SchemaMetadataInfo metadataInfo) {
        if (metadataInfo.containsProperty(ADAPTER_NAME_PROPERTY_KEY)) {
            return metadataInfo.getProperty(ADAPTER_NAME_PROPERTY_KEY);
        } else {
            LOGGER.severe("Missing adapter name trying to parse metadata information.");
            return "UNKNOWN";
        }
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