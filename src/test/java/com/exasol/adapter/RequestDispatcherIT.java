package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemErrGuard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.parser.RequestParserException;

@ExtendWith(SystemErrGuard.class)
class RequestDispatcherIT {
    private static final String DEFAULT_REQUEST_PARTS = "\"schemaMetadataInfo\" :\n" //
            + "    {\n" //
            + "        \"name\" : \"foo\",\n" //
            + "        \"properties\" :\n" //
            + "        {\n" //
            + "            \"SQL_DIALECT\" : \"MOCKADAPTER\"\n" //
            + "        }\n" //
            + "    }\n";
    private final ExaMetadata metadata = null;

    @Test
    void testDispatchCreateVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"createVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}";
        final String response = RequestDispatcher.adapterCall(null, rawRequest);
        assertEquals(response,
                "{\"type\":\"createVirtualSchema\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
    }

    @Test
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"dropVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}";
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertEquals(response, "{\"type\":\"dropVirtualSchema\"}");
    }

    @Test
    void testDispatchRefreshRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"refresh\", " + DEFAULT_REQUEST_PARTS + "}";
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertEquals(response, "{\"type\":\"refresh\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
    }

    @Test
    void testDispatchSetPropertiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"setProperties\", " + DEFAULT_REQUEST_PARTS + "}";
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertEquals(response, "{\"type\":\"setProperties\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}");
    }

    @Test
    void testDispatchGetCapabilitiesRequest() throws AdapterException {
        final String rawRequest = "{ \"type\" : \"getCapabilities\", " + DEFAULT_REQUEST_PARTS + "}";
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertEquals(response, "{\"type\":\"getCapabilities\",\"capabilities\":[]}");
    }

    @Test
    void testDispatchPushDownRequest() throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"pushdown\",\n" //
                + "    " + DEFAULT_REQUEST_PARTS + ",\n" //
                + "    \"pushdownRequest\" :\n" //
                + "    {\n" //
                + "        \"type\" : \"select\",\n" //
                + "        \"from\" :\n" //
                + "        {\n" //
                + "             \"type\" : \"table\",\n" //
                + "             \"name\" : \"FOO\"\n" //
                + "        }\n" //
                + "    },\n" //
                + "    \"involvedTables\" :\n" //
                + "    [\n" //
                + "        {\n" //
                + "            \"name\" : \"FOO\",\n" //
                + "            \"columns\" :\n" //
                + "            [\n" //
                + "                {\n" //
                + "                    \"name\" : \"BAR\"," //
                + "                    \"dataType\" :\n" //
                + "                    {\n" //
                + "                        \"type\" : \"DECIMAL\",\n" //
                + "                        \"precision\" : 18,\n" //
                + "                        \"scale\" : 0\n" //
                + "                    }\n" //
                + "                }\n" //
                + "            ]\n" //
                + "        }\n" //
                + "    ]\n" //
                + "}";
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertEquals(response, "{\"type\":\"pushdown\",\"sql\":\"SELECT * FROM FOOBAR\"}");
    }

    @Test
    void testLoggingSetAccordingProperties(final Capturable stream) throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "          \"name\" : \"REMOTE_DEBUG_TEST\",\n" //
                + "          \"properties\" :\n" //
                + "          {\n" //
                + "              \"SQL_DIALECT\" : \"MOCKADAPTER\",\n" //
                + "              \"LOG_LEVEL\" : \"FINE\"\n" //
                + "          }\n" //
                + "    }\n" //
                + "}";
//        final SchemaMetadata metadata = createSchemaMetadata(rawRequest);
//        when(this.mockAdapter.createVirtualSchema(any(), any()))
//                .thenReturn(CreateVirtualSchemaResponse.builder().schemaMetadata(metadata).build());
        stream.capture();
        final String response = RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(stream.getCapturedData(), containsString("level FINE."));
    }

    @Test
    void testRemoteLoggingSetAccordingPropertiesWithUnknownHostAndFallback(final Capturable stream)
            throws AdapterException {
        final String rawRequest = "{\n" //
                + "    \"type\" : \"createVirtualSchema\",\n" //
                + "    \"schemaMetadataInfo\" :\n" //
                + "    {\n" //
                + "          \"name\" : \"REMOTE_DEBUG_TEST\",\n" //
                + "          \"properties\" :\n" //
                + "          {\n" //
                + "              \"SQL_DIALECT\" : \"MOCKADAPTER\",\n" //
                + "              \"DEBUG_ADDRESS\" : \"this.host.does.not.exist.exasol.com\"\n" //
                + "          }\n" //
                + "    }\n" //
                + "}";
        stream.capture();
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
        assertThat(stream.getCapturedData(), containsString("Falling back to console log."));
    }

    @Test
    void testUnknownRequestTypeThrowsException(final Capturable stream) throws AdapterException {
        final String rawRequest = "{ \"type\" : \"NON_EXISTENT_REQUEST_TYPE\" }";
        stream.capture();
        assertAll(
                () -> assertThrows(RequestParserException.class,
                        () -> RequestDispatcher.adapterCall(this.metadata, rawRequest)),
                () -> assertThat(stream.getCapturedData(), containsString("SEVERE")));
    }
}