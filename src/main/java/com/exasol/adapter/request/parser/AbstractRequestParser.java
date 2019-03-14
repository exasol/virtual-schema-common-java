package com.exasol.adapter.request.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonReader;

/**
 * Abstract base class for parsers reading fragments of the Virtual Schema
 * requests.
 */
class AbstractRequestParser {
    /**
     * Create a JSON reader for raw request data.
     *
     * <p>
     * The data stream fed into this reader must be UTF-8 encoded.
     * 
     * @param rawRequest raw JSON string representing an adapter request or part
     *                   thereof
     * @return JSON reader
     */
    protected JsonReader createJsonReader(final String rawRequest) {
        final ByteArrayInputStream rawRequestStream = new ByteArrayInputStream(
                rawRequest.getBytes(StandardCharsets.UTF_8));
        final JsonReader reader = Json.createReader(rawRequestStream);
        return reader;
    }
}