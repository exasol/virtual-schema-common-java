package com.exasol.adapter.response.converter;

import static com.exasol.adapter.JsonAssertions.assertJsonMatches;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.*;

class ResponseJsonConverterTest {
    private ResponseJsonConverter responseJsonConverter;

    @BeforeEach
    void setUp() {
        this.responseJsonConverter = ResponseJsonConverter.getInstance();
    }

    @Test
    void testConvertCreateVirtualSchemaResponse() {
        final CreateVirtualSchemaResponse.Builder builder = CreateVirtualSchemaResponse.builder();
        final CreateVirtualSchemaResponse createResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        assertJsonMatches("{\"type\":\"createVirtualSchema\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertCreateVirtualSchemaResponse(createResponse));
    }

    @Test
    void testConvertDropVirtualSchemaResponse() {
        final DropVirtualSchemaResponse dropResponse = DropVirtualSchemaResponse.builder().build();
        assertJsonMatches("{\"type\":\"dropVirtualSchema\"}",
                this.responseJsonConverter.convertDropVirtualSchemaResponse(dropResponse));
    }

    @Test
    void testConvertPushDownResponse() {
        final PushDownResponse.Builder builder = PushDownResponse.builder();
        final PushDownResponse pushDownResponse = builder.pushDownSql("PUSH DOWN").build();
        assertJsonMatches("{\"type\":\"pushdown\"," //
                + "\"sql\":\"PUSH DOWN\"}", //
                this.responseJsonConverter.convertPushDownResponse(pushDownResponse));
    }

    @Test
    void testConvertGetCapabilitiesResponse() {
        final Capabilities capabilities = Capabilities.builder() //
                .addMain(MainCapability.LIMIT) //
                .addLiteral(LiteralCapability.DATE) //
                .addPredicate(PredicateCapability.EQUAL) //
                .addScalarFunction(ScalarFunctionCapability.ADD) //
                .addAggregateFunction(AggregateFunctionCapability.AVG) //
                .build();
        final GetCapabilitiesResponse.Builder builder = GetCapabilitiesResponse.builder();
        final GetCapabilitiesResponse getCapabilitiesResponse = builder.capabilities(capabilities).build();
        assertJsonMatches("{\"type\":\"getCapabilities\"," //
                + "\"capabilities\":[\"LIMIT\"," //
                + "\"LITERAL_DATE\"," //
                + "\"FN_PRED_EQUAL\", " //
                + "\"FN_AGG_AVG\", " //
                + "\"FN_ADD\"]}", //
                this.responseJsonConverter.convertGetCapabilitiesResponse(getCapabilitiesResponse));
    }

    @Test
    void testConvertRefreshResponse() {
        final RefreshResponse.Builder builder = RefreshResponse.builder();
        final RefreshResponse refreshResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        assertJsonMatches("{\"type\":\"refresh\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertRefreshResponse( //
                        refreshResponse));
    }

    @Test
    void testConvertSetPropertiesResponse() {
        final SetPropertiesResponse.Builder builder = SetPropertiesResponse.builder();
        final SetPropertiesResponse setPropertiesResponse = builder
                .schemaMetadata(new SchemaMetadata("notes", Collections.emptyList())).build();
        assertJsonMatches("{\"type\":\"setProperties\"," //
                + "\"schemaMetadata\":{\"tables\":[]," //
                + "\"adapterNotes\":\"notes\"}}", //
                this.responseJsonConverter.convertSetPropertiesResponse(setPropertiesResponse));
    }

    @Test
    void testConvertSetPropertiesResponseWhenMetadataIsNull() {
        final SetPropertiesResponse.Builder builder = SetPropertiesResponse.builder();
        final SetPropertiesResponse setPropertiesResponse = builder.build();
        assertJsonMatches("{\"type\":\"setProperties\"}",
                this.responseJsonConverter.convertSetPropertiesResponse(setPropertiesResponse));
    }
}
