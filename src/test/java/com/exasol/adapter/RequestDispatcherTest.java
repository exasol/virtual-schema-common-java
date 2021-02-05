package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.logging.*;

import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemErrGuard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.parser.RequestParserException;

@ExtendWith(SystemErrGuard.class)
class RequestDispatcherTest {
    private static final String DEFAULT_REQUEST_PARTS = "\"schemaMetadataInfo\" :\n" //
            + "    {\n" //
            + "        \"name\" : \"foo\",\n" //
            + "        \"properties\" :\n" //
            + "        {\n" //
            + "            \"SQL_DIALECT\" : \"MOCKADAPTER\"\n" //
            + "        }\n" //
            + "    }\n";
    private final ExaMetadata metadata = null;

    @BeforeEach
    void beforeEach() {
        resetRootLogger();
    }

    /**
     * @implNote this is a required work around for log verifying test cases (see
     *           https://github.com/itsallcode/junit5-system-extensions/issues/20)
     */
    private void resetRootLogger() {
        final Logger rootLogger = LogManager.getLogManager().getLogger("");
        for (final Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new ConsoleHandler());
    }

    @Test
    void testDispatchCreateVirtualSchemaRequest() throws AdapterException {
        adapterCall("{ \"type\" : \"createVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}")
                .withResponse(
                        "{\"type\":\"createVirtualSchema\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}")
                .verify();
    }

    @Test
    void testDispatchDropVirtualSchemaRequest() throws AdapterException {
        adapterCall("{ \"type\" : \"dropVirtualSchema\", " + DEFAULT_REQUEST_PARTS + "}")
                .withResponse("{\"type\":\"dropVirtualSchema\"}").verify();
    }

    @Test
    void testDispatchRefreshRequest() throws AdapterException {
        adapterCall("{ \"type\" : \"refresh\", " + DEFAULT_REQUEST_PARTS + "}")
                .withResponse("{\"type\":\"refresh\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}")
                .verify();
    }

    @Test
    void testDispatchSetPropertiesRequest() throws AdapterException {
        adapterCall("{ \"type\" : \"setProperties\", " + DEFAULT_REQUEST_PARTS + "}")
                .withResponse("{\"type\":\"setProperties\",\"schemaMetadata\":{\"tables\":[],\"adapterNotes\":\"\"}}")
                .verify();
    }

    @Test
    void testDispatchGetCapabilitiesRequest() throws AdapterException {
        adapterCall("{ \"type\" : \"getCapabilities\", " + DEFAULT_REQUEST_PARTS + "}")
                .withResponse("{\"type\":\"getCapabilities\",\"capabilities\":[]}").verify();
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
        adapterCall(rawRequest).withResponse("{\"type\":\"pushdown\",\"sql\":\"SELECT * FROM FOOBAR\"}").verify();
    }

    private AdapterCallVerifier adapterCall(final String rawRequest) {
        return new AdapterCallVerifier(rawRequest);
    }

    private class AdapterCallVerifier {
        private final String rawRequest;
        private String expectedResponse;

        private AdapterCallVerifier(final String rawRequest) {
            this.rawRequest = rawRequest;
        }

        private AdapterCallVerifier withResponse(final String expectedResponse) {
            this.expectedResponse = expectedResponse;
            return this;
        }

        private void verify() throws AdapterException {
            assertThat(RequestDispatcher.adapterCall(null, this.rawRequest), equalTo(this.expectedResponse));
        }
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
        stream.capture();
        RequestDispatcher.adapterCall(this.metadata, rawRequest);
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