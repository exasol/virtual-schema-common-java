package com.exasol.adapter.request.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;

import javax.json.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

class SchemaMetadataParserTest {
    @Test
    void testParseNameOnly() {
        final String rawSchemaMetadata = "{ \"name\": \"SCHEMA_NAME\" }";
        final SchemaMetadataInfo metadata = parseSchemaMetadata(rawSchemaMetadata);
        assertAll(() -> assertThat(metadata.getSchemaName(), equalTo("SCHEMA_NAME")),
                () -> assertThat("Empty properties", metadata.getProperties().isEmpty(), equalTo(true)),
                () -> assertThat("Empty adapter notes", metadata.getAdapterNotes(), emptyString()));
    }

    private SchemaMetadataInfo parseSchemaMetadata(final String rawSchemaMetadata) {
        final JsonReader reader = Json.createReader(new ByteArrayInputStream(rawSchemaMetadata.getBytes()));
        final JsonObject schemaMetadataInfoAsJson = reader.readObject();
        final SchemaMetadataInfoParser parser = new SchemaMetadataInfoParser();
        return parser.parse(schemaMetadataInfoAsJson);
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
        final SchemaMetadataInfo metadata = parseSchemaMetadata(rawSchemaMetadata);
        assertAll(() -> assertThat(metadata.getSchemaName(), equalTo("SCHEMA_NAME")),
                () -> assertThat(metadata.getProperties(), hasEntry(equalTo("PROP_1"), equalTo("property value 1"))),
                () -> assertThat(metadata.getAdapterNotes(), startsWith("{\"lastRefreshed")));
    }

    @Test
    void testParseAdapterNotesString() {
        final String rawSchemaMetadata = "{\n" //
                + "    \"name\": \"SCHEMA_NAME\",\n" //
                + "    \"adapterNotes\": \"this is a string\"\n" //
                + "}";
        final SchemaMetadataInfo metadata = parseSchemaMetadata(rawSchemaMetadata);
        assertThat(metadata.getAdapterNotes(), equalTo("this is a string"));
    }

    @Test
    void testParseAdapterNotesThrowsExceptionIfInteger() {
        final String rawSchemaMetadata = "{\n" //
                + "    \"name\": \"SCHEMA_NAME\",\n" //
                + "    \"adapterNotes\": 0\n" //
                + "}";
        assertThrows(IllegalArgumentException.class, () -> parseSchemaMetadata(rawSchemaMetadata));
    }
}