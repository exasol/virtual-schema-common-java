package com.exasol.adapter.json;

import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.utils.JsonHelper;

import javax.json.*;

public class ResponseJsonSerializer {

    public static final String SCALAR_FUNCTION_PREFIX = "FN_";
    public static final String PREDICATE_PREFIX = "FN_PRED_";
    public static final String AGGREGATE_FUNCTION_PREFIX = "FN_AGG_";
    public static final String LITERAL_PREFIX = "LITERAL_";

    private static final String SCHEMA_METADATA = "schemaMetadata";

    private ResponseJsonSerializer() {
        //Intentionally left blank
    }

    public static String makeCreateVirtualSchemaResponse(SchemaMetadata remoteMeta) {
        JsonObject res = Json.createObjectBuilder()
              .add("type", "createVirtualSchema")
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta))
              .build();
        return res.toString();
    }

    public static String makeDropVirtualSchemaResponse() {
        JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        JsonObject res = factory.createObjectBuilder()
              .add("type", "dropVirtualSchema")
              .build();
        return res.toString();
    }

    public static String makeGetCapabilitiesResponse(Capabilities capabilities) {
        JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        JsonObjectBuilder builder = factory.createObjectBuilder()
              .add("type", "getCapabilities");
        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
        for (MainCapability capability : capabilities.getMainCapabilities()) {
            String capName = capability.name();
            arrayBuilder.add(capName);
        }
        for (ScalarFunctionCapability function : capabilities.getScalarFunctionCapabilities()) {
            String capName = SCALAR_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
        for (PredicateCapability predicate : capabilities.getPredicateCapabilities()) {
            String capName = PREDICATE_PREFIX + predicate.name();
            arrayBuilder.add(capName);
        }
        for (AggregateFunctionCapability function : capabilities.getAggregateFunctionCapabilities()) {
            String capName = AGGREGATE_FUNCTION_PREFIX + function.name();
            arrayBuilder.add(capName);
        }
        for (LiteralCapability literal : capabilities.getLiteralCapabilities()) {
            String capName = LITERAL_PREFIX + literal.name();
            arrayBuilder.add(capName);
        }
        builder.add("capabilities", arrayBuilder);
        return builder.build().toString();
    }

    public static String makePushdownResponse(String pushdownSql) {
        JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        JsonObject res = factory.createObjectBuilder()
              .add("type", "pushdown")
              .add("sql", pushdownSql)
              .build();
        return res.toString();
    }

    public static String makeSetPropertiesResponse(SchemaMetadata remoteMeta) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", "setProperties");
        if (remoteMeta != null) {
            builder.add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta));
        }
        return builder.build().toString();
    }

    public static String makeRefreshResponse(SchemaMetadata remoteMeta) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", "refresh");
        builder.add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(remoteMeta));
        return builder.build().toString();
    }

}
