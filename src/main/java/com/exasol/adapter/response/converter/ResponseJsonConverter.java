package com.exasol.adapter.response.converter;

import com.exasol.adapter.response.DropVirtualSchemaResponse;
import com.exasol.utils.JsonHelper;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

/**
 * Converts response into Json format
 */
public final class ResponseJsonConverter {
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
     * Converts drop response into a Json format
     *
     * @param dropResponse instance
     * @return string representation of a JsonObject
     */
    @SuppressWarnings("squid:S1172")
    public String convert(final DropVirtualSchemaResponse dropResponse) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObject res = factory.createObjectBuilder().add("type", "dropVirtualSchema").build();
        return res.toString();
    }
}
