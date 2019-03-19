package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.PROPERTIES_KEY;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.json.*;

/**
 * Abstract base class for parsers reading fragments of the Virtual Schema requests.
 */
class AbstractRequestParser {
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

    protected Map<String, String> parseProperties(final JsonObject root) {
        if (root.containsKey(PROPERTIES_KEY)) {
            return convertJsonObjectsToPropertyMap(root);
        } else {
            return Collections.emptyMap();
        }
    }

    private Map<String, String> convertJsonObjectsToPropertyMap(final JsonObject root) {
        final Map<String, String> properties;
        properties = new HashMap<>();
        final JsonObject jsonProperties = root.getJsonObject(PROPERTIES_KEY);
        for (final String key : jsonProperties.keySet()) {
            properties.put(key, jsonProperties.getString(key));
        }
        return properties;
    }
}