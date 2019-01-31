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
public class JsonHelper {

    public static JsonBuilderFactory getBuilderFactory() {
        Map<String, Object> config = new HashMap<String, Object>();
        return Json.createBuilderFactory(config);
    }

    public static JsonObject getJsonObject(String data) {
        JsonReader jr = Json.createReader(new StringReader(data));
        JsonObject obj = jr.readObject();
        jr.close();
        return obj;
    }

    public static String getKeyAsString(JsonObject obj, String key, String defaultValue) {
        String value = defaultValue;
        if (obj.containsKey(key)) {
            value = obj.get(key).toString();
        }
        return value;
    }

    public static String prettyJson(JsonObject obj) {
        Map<String, Boolean> config = new HashMap<String, Boolean>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        StringWriter strWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(strWriter);
        try (JsonWriter jsonWriter = Json.createWriterFactory(config).createWriter(pw)) {
            jsonWriter.writeObject(obj);
            return strWriter.toString();
        }
    }

}
