package com.exasol.adapter.json;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.utils.JsonHelper;

import javax.json.*;

public final class ResponseJsonSerializer {
    public static final String SCALAR_FUNCTION_PREFIX = "FN_";
    public static final String PREDICATE_PREFIX = "FN_PRED_";
    public static final String AGGREGATE_FUNCTION_PREFIX = "FN_AGG_";
    public static final String LITERAL_PREFIX = "LITERAL_";
    private static final String SCHEMA_METADATA = "schemaMetadata";

    private ResponseJsonSerializer() {
        //Intentionally left blank
    }

    public static String makeCreateVirtualSchemaResponse(final SchemaMetadata remoteMeta) {
        final JsonObject res = Json.createObjectBuilder()
              .add("type", "createVirtualSchema")
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta))
              .build();
        return res.toString();
    }

    public static String makeDropVirtualSchemaResponse() {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject res = factory.createObjectBuilder()
              .add("type", "dropVirtualSchema")
              .build();
        return res.toString();
    }

    public static String makeGetCapabilitiesResponse(final Capabilities capabilities) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObjectBuilder builder = factory.createObjectBuilder()
              .add("type", "getCapabilities");
        final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
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

    public static String makePushdownResponse(final String pushdownSql) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject res = factory.createObjectBuilder()
              .add("type", "pushdown")
              .add("sql", pushdownSql)
              .build();
        return res.toString();
    }

    public static String makeSetPropertiesResponse(final SchemaMetadata remoteMeta) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", "setProperties");
        if (remoteMeta != null) {
            builder.add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta));
        }
        return builder.build().toString();
    }

    public static String makeRefreshResponse(final SchemaMetadata remoteMeta) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", "refresh");
        builder.add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta));
        return builder.build().toString();
    }
}
