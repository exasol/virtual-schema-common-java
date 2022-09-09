package com.exasol.adapter.request.parser.json;

import com.exasol.adapter.request.parser.json.JsonKeyValue.Complex;
import com.exasol.adapter.request.parser.json.JsonKeyValue.Simple;
import com.exasol.adapter.request.parser.json.JsonParent.JsonArray;
import com.exasol.adapter.request.parser.json.JsonParent.JsonGroup;

public interface JsonEntry {

    public static JsonGroup group(final JsonEntry... children) {
        return new JsonGroup(children);
    }

    public static JsonArray array(final JsonEntry... children) {
        return new JsonArray(children);
    }

    public static JsonKeyValue entry(final String key, final String value) {
        return new Simple(key, value, "\"");
    }

    public static JsonKeyValue entry(final String key, final JsonValue value) {
        return new Simple(key, value.render(), "");
    }

    public static JsonKeyValue entry(final String key, final double value) {
        return new Simple(key, value);
    }

    public static JsonKeyValue entry(final String key, final int value) {
        return new Simple(key, value);
    }

    public static JsonKeyValue entry(final String key, final boolean value) {
        return new Simple(key, value);
    }

    public static JsonKeyValue entry(final String key, final JsonParent value) {
        return new Complex(key, value);
    }

    public static JsonValue value(final String value) {
        return new JsonValue(value, "\"");
    }

    public static JsonValue nullValue() {
        return new JsonValue("null", "");
    }

    String render(int indent);

    default String render() {
        return render(0);
    }
}