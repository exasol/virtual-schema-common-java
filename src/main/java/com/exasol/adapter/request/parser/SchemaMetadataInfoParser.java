package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import java.util.*;

import javax.json.JsonObject;
import javax.json.JsonReader;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This parser reads the so called Schema Metadata Information sent alongside
 * each request.
 */
public class SchemaMetadataInfoParser extends AbstractRequestParser {
    /**
     * Parse a JSON structure into {@link SchemaMetadataInfo}
     *
     * @param rawSchemaMetadata excerpt from the adapter request containing the
     *                          schema metadata info as JSON string
     * @return parsed {@link SchemaMetadataInfo}
     */
    public SchemaMetadataInfo parse(final String rawSchemaMetadata) {
        final JsonReader reader = createJsonReader(rawSchemaMetadata);
        return parse(reader.readObject());
    }

    /**
     * Parse a JSON structure into {@link SchemaMetadataInfo}
     *
     * @param schemaMetadataInfoObject excerpt from the adapter request containing
     *                                 the schema metadata info as JSON object
     * @return parsed {@link SchemaMetadataInfo}
     */
    public SchemaMetadataInfo parse(final JsonObject schemaMetadataInfoObject) {
        final String schemaName = parseSchemaName(schemaMetadataInfoObject);
        final Map<String, String> properties = parseProperties(schemaMetadataInfoObject);
        final String adapterNotes = parseAdapterNotes(schemaMetadataInfoObject);
        return new SchemaMetadataInfo(schemaName, adapterNotes, properties);
    }

    private String parseSchemaName(final JsonObject root) {
        return root.getString(SCHEMA_NAME_KEY);
    }

    private String parseAdapterNotes(final JsonObject root) {
        if (root.containsKey(ADAPTER_NOTES_KEY)) {
            return root.getJsonObject(ADAPTER_NOTES_KEY).toString();
        } else {
            return "";
        }
    }
}