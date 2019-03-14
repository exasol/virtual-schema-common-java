package com.exasol.adapter.response.converter;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.json.SchemaMetadataSerializer;
import com.exasol.adapter.response.CreateVirtualSchemaResponse;
import com.exasol.adapter.response.DropVirtualSchemaResponse;
import com.exasol.adapter.response.GetCapabilitiesResponse;
import com.exasol.adapter.response.PushDownResponse;
import com.exasol.utils.JsonHelper;

import javax.json.*;

/**
 * Converts response into JSON format
 */
public final class ResponseJsonConverter {
    private static final String SCALAR_FUNCTION_PREFIX = "FN_";
    private static final String PREDICATE_PREFIX = "FN_PRED_";
    private static final String AGGREGATE_FUNCTION_PREFIX = "FN_AGG_";
    private static final String LITERAL_PREFIX = "LITERAL_";
    private static final String SCHEMA_METADATA = "schemaMetadata";
    private static final ResponseJsonConverter responseJsonConverter = new ResponseJsonConverter();

    private ResponseJsonConverter() {
        //intentionally left blank
    }

    /**
     * Returns instance of {@link DropVirtualSchemaResponse} singleton class
     *
     * @return {@link DropVirtualSchemaResponse} instance
     */
    public static ResponseJsonConverter getInstance() {
        return responseJsonConverter;
    }

    /**
     * Converts drop virtual schema response into a JSON format
     *
     * @param dropResponse instance
     * @return string representation of a JSON Object
     */
    @SuppressWarnings("squid:S1172")
    public String convertDropVirtualSchemaResponse(final DropVirtualSchemaResponse dropResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject response = factory.createObjectBuilder() //
              .add("type", "dropVirtualSchema") //
              .build();
        return response.toString();
    }

    /**
     * Converts create virtual schema response into a JSON format
     *
     * @param createResponse instance
     * @return string representation of a JSON Object
     */
    public String convertCreateVirtualSchemaResponse(final CreateVirtualSchemaResponse createResponse) {
        final JsonObject response = Json.createObjectBuilder() //
              .add("type", "createVirtualSchema") //
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(createResponse.getSchemaMetadata())) //
              .build();
        return response.toString();
    }

    /**
     * Converts push down response into a JSON format
     *
     * @param pushDownResponse instance
     * @return string representation of a JSON Object
     */
    public String convertPushDownResponse(final PushDownResponse pushDownResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject res = factory.createObjectBuilder() //
              .add("type", "pushdown") //
              .add("sql", pushDownResponse.getPushDownSql()) //
              .build();
        return res.toString();
    }

    /**
     * Converts get capabilities response into a JSON format
     *
     * @param getCapabilitiesResponse instance
     * @return string representation of a JSON Object
     */
    public String convertGetCapabilitiesResponse(final GetCapabilitiesResponse getCapabilitiesResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObjectBuilder builder = factory.createObjectBuilder().add("type", "getCapabilities");
        final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
        final Capabilities capabilities = getCapabilitiesResponse.getCapabilities();
        for (final MainCapability capability : capabilities.getMainCapabilities()) {
            final String capName = capability.name();
            arrayBuilder.add(capName);
        }
        for (final ScalarFunctionCapability function : capabilities.getScalarFunctionCapabilities()) {
            final String capName = SCALAR_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
        for (final PredicateCapability predicate : capabilities.getPredicateCapabilities()) {
            final String capName = PREDICATE_PREFIX + predicate.name();
            arrayBuilder.add(capName);
        }
        for (final AggregateFunctionCapability function : capabilities.getAggregateFunctionCapabilities()) {
            final String capName = AGGREGATE_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
        for (final LiteralCapability literal : capabilities.getLiteralCapabilities()) {
            final String capName = LITERAL_PREFIX + literal.name();
            arrayBuilder.add(capName);
        }
        builder.add("capabilities", arrayBuilder);
        return builder.build().toString();
    }
}
