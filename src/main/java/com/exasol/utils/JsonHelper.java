package com.exasol.utils;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * http://docs.oracle.com/javaee/7/api/javax/json/JsonObjectBuilder.html
 * http://docs.oracle.com/javaee/7/api/javax/json/stream/JsonGenerator.html
 */
public final class JsonHelper {
    private JsonHelper() {
        //Intentionally left blank
    }

    public static JsonBuilderFactory getBuilderFactory() {
        final Map<String, Object> config = new HashMap<>();
        return Json.createBuilderFactory(config);
    }

    public static JsonObject getJsonObject(final String data) {
        try (final JsonReader jr = Json.createReader(new StringReader(data))) {
            return jr.readObject();
        }
    }

    public static String getKeyAsString(final JsonObject obj, final String key, final String defaultValue) {
        String value = defaultValue;
        if (obj.containsKey(key)) {
            value = obj.get(key).toString();
        }
        return value;
    }

    public static String prettyJson(final JsonObject obj) {
        final Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        final StringWriter strWriter = new StringWriter();
        final PrintWriter pw = new PrintWriter(strWriter);
        try (final JsonWriter jsonWriter = Json.createWriterFactory(config).createWriter(pw)) {
            jsonWriter.writeObject(obj);
        }
        return strWriter.toString();
    }
}
