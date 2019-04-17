package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.PROPERTIES_KEY;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.json.*;
import javax.json.JsonValue.ValueType;

/**
 * Abstract base class for parsers reading fragments of the Virtual Schema requests.
 */
class AbstractRequestParser {
    private static final Logger LOGGER = Logger.getLogger(AbstractRequestParser.class.getName());

    /**
     * Create a JSON reader for raw request data.
     *
     * <p>
     * The data stream fed into this reader must be UTF-8 encoded.
     *
     * @param rawRequest raw JSON string representing an adapter request or part thereof
     * @return JSON reader
     */
    protected JsonReader createJsonReader(final String rawRequest) {
        final ByteArrayInputStream rawRequestStream = new ByteArrayInputStream(
                rawRequest.getBytes(StandardCharsets.UTF_8));
        return Json.createReader(rawRequestStream);
    }

    protected Map<String, String> parseProperties(final JsonObject jsonSchemaMedadataInfo) {
        if (jsonSchemaMedadataInfo.containsKey(PROPERTIES_KEY)) {
            return convertJsonObjectsToPropertyMap(jsonSchemaMedadataInfo);
        } else {
            return Collections.emptyMap();
        }
    }

    private Map<String, String> convertJsonObjectsToPropertyMap(final JsonObject root) {
        final Map<String, String> properties;
        properties = new HashMap<>();
        final JsonObject jsonProperties = root.getJsonObject(PROPERTIES_KEY);
        for (final Entry<String, JsonValue> entry : jsonProperties.entrySet()) {
            addProperty(properties, entry);
        }
        return properties;
    }

    private void addProperty(final Map<String, String> properties, final Entry<String, JsonValue> entry) {
        final String key = entry.getKey();
        final JsonValue value = entry.getValue();
        final ValueType type = value.getValueType();
        String stringValue;
        switch (type) {
        case STRING:
            stringValue = ((JsonString) value).getString();
            break;
        case NUMBER:
            stringValue = ((JsonNumber) value).toString();
            break;
        case TRUE:
            stringValue = "true";
            break;
        case FALSE:
            stringValue = "false";
            break;
        case NULL:
            stringValue = null;
            break;
        default:
            throw new IllegalArgumentException("Unable to parse adapter property value of type \"" + type
                    + "\". Supported types are strings, booleans, numbers and NULL.");
        }
        LOGGER.finer(() -> "Parsed property: \"" + key + "\" = \"" + stringValue + "\"");
        properties.put(key, stringValue);
    }
}