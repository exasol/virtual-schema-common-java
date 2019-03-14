package com.exasol.adapter.request.parser;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

class SchemaMetadataParserTest {
    @Test
    void testParseNameOnly() {
        final String rawSchemaMetadata = "{ \"name\": \"SCHEMA_NAME\" }";
        final SchemaMetadataInfoParser parser = new SchemaMetadataInfoParser();
        final SchemaMetadataInfo metadata = parser.parse(rawSchemaMetadata);
        assertAll(() -> assertThat(metadata.getSchemaName(), equalTo("SCHEMA_NAME")),
                () -> assertThat("Empty properties", metadata.getProperties().isEmpty(), equalTo(true)),
                () -> assertThat("Empty adapter notes", metadata.getAdapterNotes(), emptyString()));
    }

    @Test
    void testParse() {
        final String rawSchemaMetadata = "{\n" //
                + "    \"name\": \"SCHEMA_NAME\",\n" //
                + "    \"adapterNotes\": {\n" //
                + "        \"lastRefreshed\": \"2015-03-01 12:10:01\",\n" //
                + "        \"key\": \"Any custom schema state here\"\n" //
                + "    },\n" //
                + "    \"properties\": {\n" //
                + "        \"PROP_1\": \"property value 1\",\n" //
                + "        \"PROP_2\": \"property value 2\"\n" //
                + "    }\n" //
                + "}";
        final SchemaMetadataInfoParser parser = new SchemaMetadataInfoParser();
        final SchemaMetadataInfo metadata = parser.parse(rawSchemaMetadata);
        assertAll(() -> assertThat(metadata.getSchemaName(), equalTo("SCHEMA_NAME")),
                () -> assertThat(metadata.getProperties(), hasEntry(equalTo("PROP_1"), equalTo("property value 1"))),
                () -> assertThat(metadata.getAdapterNotes(), startsWith("{\"lastRefreshed")));
    }
}