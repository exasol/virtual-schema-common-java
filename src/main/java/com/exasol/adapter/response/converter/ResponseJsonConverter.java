package com.exasol.adapter.response.converter;

import javax.json.*;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.converter.SchemaMetadataJsonConverter;
import com.exasol.adapter.response.*;

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
     * Returns instance of {@link ResponseJsonConverter} singleton class
     *
     * @return {@link ResponseJsonConverter} instance
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
                .add(SCHEMA_METADATA,
                        SchemaMetadataJsonConverter.getInstance().convert(createResponse.getSchemaMetadata())) //
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
        addMainCapabilitiesToBuilder(capabilities, arrayBuilder);
        addScalarFunctionCapabilitiesToBuilder(capabilities, arrayBuilder);
        addPredicateCapabilitiesToBuilder(capabilities, arrayBuilder);
        addAggregateCapabilitiesToBuilder(capabilities, arrayBuilder);
        addLiteralCapabilitiesToBuilder(capabilities, arrayBuilder);
        builder.add("capabilities", arrayBuilder);
        return builder.build().toString();
    }

    private void addLiteralCapabilitiesToBuilder(final Capabilities capabilities, final JsonArrayBuilder arrayBuilder) {
        for (final LiteralCapability literal : capabilities.getLiteralCapabilities()) {
            final String capName = LITERAL_PREFIX + literal.name();
            arrayBuilder.add(capName);
        }
    }

    private void addAggregateCapabilitiesToBuilder(final Capabilities capabilities,
            final JsonArrayBuilder arrayBuilder) {
        for (final AggregateFunctionCapability function : capabilities.getAggregateFunctionCapabilities()) {
            final String capName = AGGREGATE_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
    }

    private void addPredicateCapabilitiesToBuilder(final Capabilities capabilities,
            final JsonArrayBuilder arrayBuilder) {
        for (final PredicateCapability predicate : capabilities.getPredicateCapabilities()) {
            final String capName = PREDICATE_PREFIX + predicate.name();
            arrayBuilder.add(capName);
        }
    }

    private void addScalarFunctionCapabilitiesToBuilder(final Capabilities capabilities,
            final JsonArrayBuilder arrayBuilder) {
        for (final ScalarFunctionCapability function : capabilities.getScalarFunctionCapabilities()) {
            final String capName = SCALAR_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
    }

    private void addMainCapabilitiesToBuilder(final Capabilities capabilities, final JsonArrayBuilder arrayBuilder) {
        for (final MainCapability capability : capabilities.getMainCapabilities()) {
            final String capName = capability.name();
            arrayBuilder.add(capName);
        }
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
                .add(SCHEMA_METADATA,
                        SchemaMetadataJsonConverter.getInstance().convert(refreshResponse.getSchemaMetadata())) //
                .build() //
                .toString();
    }

    /**
     * Converts set properties response into a JSON format
     *
     * @param setPropertiesResponse instance
     * @return string representation of a JSON Object
     */
    public String convertSetPropertiesResponse(final SetPropertiesResponse setPropertiesResponse) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", "setProperties");
        if (setPropertiesResponse.getSchemaMetadata() != null) {
            builder.add(SCHEMA_METADATA,
                    SchemaMetadataJsonConverter.getInstance().convert(setPropertiesResponse.getSchemaMetadata()));
        }
        return builder.build().toString();
    }
}
