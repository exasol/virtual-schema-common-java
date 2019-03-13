package com.exasol.adapter.response.converter;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.CreateVirtualSchemaResponse;
import com.exasol.adapter.response.DropVirtualSchemaResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;

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

    @Test
    void testMakeCreateVirtualSchemaResponse() throws JSONException {
        final CreateVirtualSchemaResponse.Builder builder = CreateVirtualSchemaResponse.builder();
        final CreateVirtualSchemaResponse createResponse = builder.schemaMetadata(new SchemaMetadata("notes",
              Collections.emptyList())).build();
        JSONAssert.assertEquals("{\"type\":\"createVirtualSchema\"," //
                    + "\"schemaMetadata\":{\"tables\":[]," //
                    + "\"adapterNotes\":\"notes\"}}", //
              this.responseJsonConverter.convert(createResponse), false);
    }
}
