package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.ADAPTER_NOTES_KEY;
import static com.exasol.adapter.request.parser.RequestParserConstants.SCHEMA_NAME_KEY;

import java.util.Map;

import javax.json.*;
import javax.json.JsonValue.ValueType;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This parser reads the so called Schema Metadata Information sent alongside each request.
 */
public class SchemaMetadataInfoParser extends AbstractRequestParser {
    /**
     * Parse a JSON structure into {@link SchemaMetadataInfo}
     *
     * @param schemaMetadataInfoObject excerpt from the adapter request containing the schema metadata info as JSON
     *                                 object
     * @return parsed {@link SchemaMetadataInfo}
     */
    public SchemaMetadataInfo parse(final JsonObject schemaMetadataInfoObject) {
        final String schemaName = parseSchemaName(schemaMetadataInfoObject);
        final Map<String, String> properties = parseProperties(schemaMetadataInfoObject);
        final String adapterNotes = parseAdapterNotes(schemaMetadataInfoObject);
        return new SchemaMetadataInfo(schemaName, adapterNotes, properties);
    }

    private String parseSchemaName(final JsonObject schemaMetadataInfo) {
        return schemaMetadataInfo.getString(SCHEMA_NAME_KEY);
    }

    private String parseAdapterNotes(final JsonObject schemaMetadataInfo) {
        if (schemaMetadataInfo.containsKey(ADAPTER_NOTES_KEY)) {
            final JsonValue adapterNotes = schemaMetadataInfo.get(ADAPTER_NOTES_KEY);
            final ValueType type = adapterNotes.getValueType();
            switch (type) {
            case STRING:
                return ((JsonString) adapterNotes).getString();
            case OBJECT:
                return adapterNotes.toString();
            default:
                throw new IllegalArgumentException(
                        "Error parsing adapter notes. Must be a JSON string or a JSON object but was type \"" + type
                                + "\".");
            }
        } else {
            return "";
        }
    }
}