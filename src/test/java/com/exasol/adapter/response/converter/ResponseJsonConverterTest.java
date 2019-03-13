package com.exasol.adapter.response.converter;

import com.exasol.adapter.response.DropVirtualSchemaResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class ResponseJsonConverterTest {
    private ResponseJsonConverter responseJsonConverter;

    @BeforeEach
    void setUp() {
        this.responseJsonConverter = ResponseJsonConverter.getInstance();
    }

    @Test
    void testMakeDropVirtualSchemaResponse() throws JSONException {
        final DropVirtualSchemaResponse dropResponse = DropVirtualSchemaResponse.builder().build();
        JSONAssert.assertEquals("{\"type\":\"dropVirtualSchema\"}", this.responseJsonConverter.convert(dropResponse),
              false);
    }
}
