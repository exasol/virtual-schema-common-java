package com.exasol.adapter.request.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RequestParserTest {
    private static final String SCHEMA_METADATA_INFO = "\"schemaMetadataInfo\" : { \"name\" : \"foo\" }";

    @Test
    void testParseThrowsExceptionIfRequestTypeUnknown() {
        final String rawRequest = "{ \"type\" : \"UNKNOWN\", " + SCHEMA_METADATA_INFO + "}";
        assertThrows(RequestParserException.class, () -> RequestParser.create().parse(rawRequest));
    }
}