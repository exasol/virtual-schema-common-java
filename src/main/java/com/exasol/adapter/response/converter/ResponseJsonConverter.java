package com.exasol.adapter.response.converter;

import com.exasol.adapter.json.SchemaMetadataSerializer;
import com.exasol.adapter.response.CreateVirtualSchemaResponse;
import com.exasol.adapter.response.DropVirtualSchemaResponse;
import com.exasol.adapter.response.PushDownResponse;
import com.exasol.utils.JsonHelper;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

/**
 * Converts response into JSON format
 */
public final class ResponseJsonConverter {
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
     * @return string representation of a JSONObject
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
     * @return string representation of a JSONObject
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
     * @return string representation of a JSONObject
     */
    public String convertPushDownResponse(final PushDownResponse pushDownResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject res = factory.createObjectBuilder() //
              .add("type", "pushdown") //
              .add("sql", pushDownResponse.getPushDownSql()) //
              .build();
        return res.toString();
    }
}
