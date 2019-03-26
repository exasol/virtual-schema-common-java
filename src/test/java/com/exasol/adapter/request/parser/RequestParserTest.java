package com.exasol.adapter.request.parser;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.*;

class RequestParserTest {
    private static final String SCHEMA_METADATA_INFO = "\"schemaMetadataInfo\" : { \"name\" : \"foo\" }";
    private RequestParser parser;

    @BeforeEach
    void beforeEach() {
        this.parser = RequestParser.create();
    }

    @Test
    void testParseThrowsExceptionIfRequestTypeUnknown() {
        final String rawRequest = "{ \"type\" : \"UNKNOWN\", \"schemaMetadataInfo\" : { \"name\" : \"foo\" } }";
        assertThrows(RequestParserException.class, () -> this.parser.parse(rawRequest));
    }

    @Test
    void testParseAdapterName() {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"setProperties\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "        \"name\" : \"foo\",\n"//
                + "        \"properties\" :\n"//
                + "        {\n" //
                + "            \"SQL_DIALECT\" : \"THE_DIALECT\"\n" //
                + "        }\n" //
                + "    }\n" //
                + "}";
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat(request.getAdapterName(), equalTo("THE_DIALECT"));
    }

    @Test
    void testParseSetPropertiesRequest() {
        final String rawRequest = "{" //
                + "    \"type\" : \"setProperties\"," //
                + "    \"properties\" :" //
                + "    {" //
                + "        \"A\" : \"value A\"," //
                + "        \"B\" : \"value B\"" //
                + "    }," //
                + SCHEMA_METADATA_INFO //
                + "}";
        final AbstractAdapterRequest request = this.parser.parse(rawRequest);
        assertThat("Request class", request, instanceOf(SetPropertiesRequest.class));
        final Map<String, String> properties = ((SetPropertiesRequest) request).getProperties();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.SET_PROPERTIES)),
                () -> assertThat(properties, aMapWithSize(2)),
                () -> assertThat(properties, hasEntry(equalTo("A"), equalTo("value A"))),
                () -> assertThat(properties, hasEntry(equalTo("B"), equalTo("value B"))));
    }

    @Test
    void testParsePushDownRequest() {
        final String rawRequest = "{" //
                + "    \"type\" : \"pushdown\",\n" //
                + "    \"pushdownRequest\" :\n" //
                + "    {\n" //
                + "        \"type\" : \"select\",\n" //
                + "        \"from\" :\n" //
                + "        {\n" //
                + "            \"name\" : \"FOO\",\n" //
                + "            \"type\" : \"table\"\n" //
                + "        }\n" //
                + "    },\n" //
                + "    \"involvedTables\" :\n" //
                + "    [\n" //
                + "        {\n" //
                + "            \"name\" : \"FOO\",\n" //
                + "            \"columns\" :\n" //
                + "            [\n" //
                + "                {\n" //
                + "                    \"name\" : \"BAR\",\n" //
                + "                    \"dataType\" :\n" //
                + "                    {\n" //
                + "                         \"precision\" : 18,\n" //
                + "                         \"scale\" : 0,\n" //
                + "                         \"type\" : \"DECIMAL\"\n" //
                + "                    }\n" //
                + "                }\n" //
                + "            ]\n" //
                + "        }\n" //
                + "    ],\n" //
                + SCHEMA_METADATA_INFO //
                + "}";
        final AbstractAdapterRequest request = this.parser.parse(rawRequest);
        assertThat("Request class", request, instanceOf(PushDownRequest.class));
        final List<TableMetadata> involvedTables = ((PushDownRequest) request).getInvolvedTablesMetadata();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.PUSHDOWN)),
                () -> assertThat(involvedTables, iterableWithSize(1)),
                () -> assertThat(involvedTables.get(0).getName(), equalTo("FOO")));
    }
}