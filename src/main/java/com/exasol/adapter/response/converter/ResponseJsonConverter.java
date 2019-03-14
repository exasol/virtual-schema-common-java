package com.exasol.adapter.response.converter;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.json.SchemaMetadataSerializer;
import com.exasol.adapter.response.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

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
        return Json.createObjectBuilder() //
              .add("type", "dropVirtualSchema") //
              .build() //
              .toString();
    }

    /**
     * Converts create virtual schema response into a JSON format
     *
     * @param createResponse instance
     * @return string representation of a JSON Object
     */
    public String convertCreateVirtualSchemaResponse(final CreateVirtualSchemaResponse createResponse) {
        return Json.createObjectBuilder() //
              .add("type", "createVirtualSchema") //
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(createResponse.getSchemaMetadata())) //
              .build() //
              .toString();
    }

    /**
     * Converts push down response into a JSON format
     *
     * @param pushDownResponse instance
     * @return string representation of a JSON Object
     */
    public String convertPushDownResponse(final PushDownResponse pushDownResponse) {
        return Json.createObjectBuilder() //
              .add("type", "pushdown") //
              .add("sql", pushDownResponse.getPushDownSql()) //
              .build() //
              .toString();
    }

    /**
     * Converts get capabilities response into a JSON format
     *
     * @param getCapabilitiesResponse instance
     * @return string representation of a JSON Object
     */
    public String convertGetCapabilitiesResponse(final GetCapabilitiesResponse getCapabilitiesResponse) {
        final JsonObjectBuilder builder = Json.createObjectBuilder().add("type", "getCapabilities");
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
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

    /**
     * Converts refresh response into a JSON format
     *
     * @param refreshResponse instance
     * @return string representation of a JSON Object
     */
    public String convertRefreshResponse(final RefreshResponse refreshResponse) {
        return Json.createObjectBuilder() //
              .add("type", "refresh") //
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(refreshResponse.getSchemaMetadata())) //
              .build() //
              .toString();
    }
}
