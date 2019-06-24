package com.exasol.adapter.response.converter;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.*;
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
    void testConvertCreateVirtualSchemaResponse() throws JSONException {
        final CreateVirtualSchemaResponse.Builder builder = CreateVirtualSchemaResponse.builder();
        final CreateVirtualSchemaResponse createResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        JSONAssert.assertEquals("{\"type\":\"createVirtualSchema\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertCreateVirtualSchemaResponse(createResponse), false);
    }

    @Test
    void testConvertDropVirtualSchemaResponse() throws JSONException {
        final DropVirtualSchemaResponse dropResponse = DropVirtualSchemaResponse.builder().build();
        JSONAssert.assertEquals("{\"type\":\"dropVirtualSchema\"}",
                this.responseJsonConverter.convertDropVirtualSchemaResponse(dropResponse), false);
    }

    @Test
    void testConvertPushDownResponse() throws JSONException {
        final PushDownResponse.Builder builder = PushDownResponse.builder();
        final PushDownResponse pushDownResponse = builder.pushDownSql("PUSH DOWN").build();
        JSONAssert.assertEquals("{\"type\":\"pushdown\"," //
                + "\"sql\":\"PUSH DOWN\"}", //
                this.responseJsonConverter.convertPushDownResponse(pushDownResponse), false);
    }

    @Test
    void testConvertGetCapabilitiesResponse() throws JSONException {
        final Capabilities capabilities = Capabilities.builder() //
                .addMain(MainCapability.LIMIT) //
                .addLiteral(LiteralCapability.DATE) //
                .addPredicate(PredicateCapability.EQUAL) //
                .addScalarFunction(ScalarFunctionCapability.ADD) //
                .addAggregateFunction(AggregateFunctionCapability.AVG) //
                .build();
        final GetCapabilitiesResponse.Builder builder = GetCapabilitiesResponse.builder();
        final GetCapabilitiesResponse getCapabilitiesResponse = builder.capabilities(capabilities).build();
        JSONAssert.assertEquals("{\"type\":\"getCapabilities\"," //
                + "\"capabilities\":[\"LIMIT\"," //
                + "\"LITERAL_DATE\"," //
                + "\"FN_PRED_EQUAL\", " //
                + "\"FN_AGG_AVG\", " //
                + "\"FN_ADD\"]}", //
                this.responseJsonConverter.convertGetCapabilitiesResponse(getCapabilitiesResponse), false);
    }

    @Test
    void testConvertRefreshResponse() throws JSONException {
        final RefreshResponse.Builder builder = RefreshResponse.builder();
        final RefreshResponse refreshResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        JSONAssert.assertEquals("{\"type\":\"refresh\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertRefreshResponse( //
                        refreshResponse),
                false);
    }

    @Test
    void testConvertSetPropertiesResponse() throws JSONException {
        final SetPropertiesResponse.Builder builder = SetPropertiesResponse.builder();
        final SetPropertiesResponse setPropertiesResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        JSONAssert.assertEquals("{\"type\":\"setProperties\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertSetPropertiesResponse(setPropertiesResponse), false);
    }

    @Test
    void testConvertSetPropertiesResponseWhenMetadataIsNull() throws JSONException {
        final SetPropertiesResponse.Builder builder = SetPropertiesResponse.builder();
        final SetPropertiesResponse setPropertiesResponse = builder.build();
        JSONAssert.assertEquals("{\"type\":\"setProperties\"}",
                this.responseJsonConverter.convertSetPropertiesResponse(setPropertiesResponse), false);
    }
}
