package com.exasol.adapter.response.converter;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.CreateVirtualSchemaResponse;
import com.exasol.adapter.response.DropVirtualSchemaResponse;
import com.exasol.adapter.response.PushDownResponse;
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
    void testConvertDropVirtualSchemaResponse() throws JSONException {
        final DropVirtualSchemaResponse dropResponse = DropVirtualSchemaResponse.builder().build();
        JSONAssert.assertEquals("{\"type\":\"dropVirtualSchema\"}",
              this.responseJsonConverter.convertDropVirtualSchemaResponse(dropResponse), false);
    }

    @Test
    void testConvertCreateVirtualSchemaResponse() throws JSONException {
        final CreateVirtualSchemaResponse.Builder builder = CreateVirtualSchemaResponse.builder();
        final CreateVirtualSchemaResponse createResponse =
              builder.schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        JSONAssert.assertEquals("{\"type\":\"createVirtualSchema\"," //
                    + "\"schemaMetadata\":{\"tables\":[]," //
                    + "\"adapterNotes\":\"notes\"}}", //
              this.responseJsonConverter.convertCreateVirtualSchemaResponse(createResponse), false);
    }

    @Test
    void testConvertPushdownResponse() throws JSONException {
        final PushDownResponse.Builder builder = PushDownResponse.builder();
        final PushDownResponse pushDownResponse = builder.pushDownSql("PUSH DOWN").build();
        JSONAssert.assertEquals("{\"type\":\"pushdown\"," //
                    + "\"sql\":\"PUSH DOWN\"}", //
              this.responseJsonConverter.convertPushDownResponse(pushDownResponse), false);
    }
}
