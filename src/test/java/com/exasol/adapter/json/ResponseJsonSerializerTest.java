package com.exasol.adapter.json;

import java.util.Collections;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.SchemaMetadata;

class ResponseJsonSerializerTest {
    @Test
    void testMakeCreateVirtualSchemaResponse() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"createVirtualSchema\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                ResponseJsonSerializer.makeCreateVirtualSchemaResponse( //
                        new SchemaMetadata("notes", //
                                Collections.emptyList())),
                false);
    }

    @Test
    void testMakeDropVirtualSchemaResponse() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"dropVirtualSchema\"}",
                ResponseJsonSerializer.makeDropVirtualSchemaResponse(), false);
    }

    @Test
    void testMakeGetCapabilitiesResponse() throws JSONException {
        final Capabilities capabilities = Capabilities.builder() //
                .addMain(MainCapability.LIMIT) //
                .addLiteral(LiteralCapability.DATE) //
                .addPredicate(PredicateCapability.EQUAL) //
                .addScalarFunction(ScalarFunctionCapability.ADD) //
                .addAggregateFunction(AggregateFunctionCapability.AVG) //
                .build();
        JSONAssert.assertEquals("{\"type\":\"getCapabilities\"," //
                + "\"capabilities\":[\"LIMIT\"," //
                + "\"LITERAL_DATE\"," //
                + "\"FN_PRED_EQUAL\", " //
                + "\"FN_AGG_AVG\", " //
                + "\"FN_ADD\"]}", //
                ResponseJsonSerializer.makeGetCapabilitiesResponse(capabilities), false);
    }

    @Test
    void testMakePushdownResponse() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"pushdown\"," //
                + "\"sql\":\"PUSH DOWN\"}", //
                ResponseJsonSerializer.makePushdownResponse("PUSH DOWN"), false);
    }

    @Test
    void testMakeSetPropertiesResponse() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"setProperties\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                ResponseJsonSerializer.makeSetPropertiesResponse(new SchemaMetadata("notes", //
                        Collections.emptyList())),
                false);
    }

    @Test
    void testMakeSetPropertiesResponseWhenMetadataIsNull() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"setProperties\"}", ResponseJsonSerializer.makeSetPropertiesResponse(null),
                false);
    }

    @Test
    void testMakeRefreshResponse() throws JSONException {
        JSONAssert.assertEquals("{\"type\":\"refresh\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                ResponseJsonSerializer.makeRefreshResponse( //
                        new SchemaMetadata("notes", Collections.emptyList())),
                false);
    }
}