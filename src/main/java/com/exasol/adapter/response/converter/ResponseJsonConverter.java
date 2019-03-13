package com.exasol.adapter.response.converter;

import com.exasol.adapter.json.SchemaMetadataSerializer;
import com.exasol.adapter.response.CreateVirtualSchemaResponse;
import com.exasol.adapter.response.DropVirtualSchemaResponse;
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
     * Converts drop response into a JSON format
     *
     * @param dropResponse instance
     * @return string representation of a JSONObject
     */
    @SuppressWarnings("squid:S1172")
    public String convert(final DropVirtualSchemaResponse dropResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject response = factory.createObjectBuilder().add("type", "dropVirtualSchema").build();
        return response.toString();
    }

    /**
     * Converts create response into a JSON format
     *
     * @param createResponse instance
     * @return string representation of a JSONObject
     */
    public String convert(final CreateVirtualSchemaResponse createResponse) {
        final JsonObject response = Json.createObjectBuilder().add("type", "createVirtualSchema")
              .add(SCHEMA_METADATA, SchemaMetadataSerializer.serialize(createResponse.getSchemaMetadata())).build();
        return response.toString();
    }
}
