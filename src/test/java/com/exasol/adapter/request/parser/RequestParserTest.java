package com.exasol.adapter.request.parser;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.request.AdapterRequest;
import com.exasol.adapter.request.SetPropertiesRequest;

class RequestParserTest {
    private static final String SCHEMA_METADATA_INFO = "\"schemaMetadataInfo\" : { \"name\" : \"foo\" }";
    private RequestParser parser;

    @BeforeEach
    void beforeEach() {
        this.parser = RequestParser.create();
    }

    @Test
    void testParseThrowsExceptionIfRequestTypeUnknown() {
        final String rawRequest = "{ \"type\" : \"UNKNOWN\", " + SCHEMA_METADATA_INFO + "}";
        assertThrows(RequestParserException.class, () -> this.parser.parse(rawRequest));
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
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat("Request class", request, instanceOf(SetPropertiesRequest.class));
        final Map<String, String> properties = ((SetPropertiesRequest) request).getProperties();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequest.AdapterRequestType.SET_PROPERTIES)),
                () -> assertThat(properties, aMapWithSize(2)),
                () -> assertThat(properties, hasEntry(equalTo("A"), equalTo("value A"))),
                () -> assertThat(properties, hasEntry(equalTo("B"), equalTo("value B"))));
    }

//    @Test
//    void testPushdownRequest() {
//        final String rawRequest = "{" //
//                + "    \"type\" : \"pushdown\"," //
//                + "    \"pushdownRequest\" :" //
//                + "    {" //
//                + "        \"type\" : \"select\"," //
//                + "        \"from\" :" //
//                + "        {" //
//                + "            \"type\" : \"table\"," //
//                + "            \"name\" : \"FOO\"" //
//                + "        }," //
//                + "        \"selectList\" :" //
//                + "        [" //
//                + "            {" //
//                + "                \"type\" : \"column\"," //
//                + "                \"name\" : \"BAR\"," //
//                + "                \"columnNr\" : 1," //
//                + "                \"tableName\" : \"FOO\"" //
//                + "            }" //
//                + "        ]" //
//                + "    }," //
//                + "    \"involvedTables\" : " //
//                + "    [" //
//                + "        {" //
//                + "            \"name\" : \"FOO\"," //
//                + "            \"columns\" :" //
//                + "            {" //
//                + "                \"name\" : \"BAR\"," //
//                + "                \"dataType\" :" //
//                + "                {" //
//                + "                    \"type\" : \"DECIMAL\"," //
//                + "                    \"precision\" : 18," //
//                + "                    \"scale\" : 0" //
//                + "                }" //
//                + "            }" //
//                + "         }" //
//                + "    ]," //
//                + SCHEMA_METADATA_INFO //
//                + "}";
//        final AdapterRequest request = this.parser.parse(rawRequest);
//        assertThat("Request class", request, instanceOf(PushdownRequest.class));
//        final PushdownRequest pushdownRequest = (PushdownRequest) request;
//        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequest.AdapterRequestType.PUSHDOWN)),
//                () -> assertThat(pushdownRequest.getSelect(), instanceOf(SqlStatementSelect.class)));
//    }
}