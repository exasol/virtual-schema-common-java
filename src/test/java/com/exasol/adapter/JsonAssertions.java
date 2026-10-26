package com.exasol.adapter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public final class JsonAssertions {
    private JsonAssertions() {
        // Utility class.
    }

    /** Matches JSONAssert's non-strict comparison: extra object fields and reordered arrays are allowed. */
    public static void assertJsonMatches(final String expected, final String actual) {
        final JsonValue expectedValue = parse(expected);
        final JsonValue actualValue = parse(actual);
        assertTrue(matches(expectedValue, actualValue),
                () -> "Expected JSON: " + expectedValue + "\nActual JSON: " + actualValue);
    }

    private static boolean matches(final JsonValue expected, final JsonValue actual) {
        if (expected.getValueType() != actual.getValueType()) {
            return false;
        }
        switch (expected.getValueType()) {
        case OBJECT:
            return matchesObject(expected.asJsonObject(), actual.asJsonObject());
        case ARRAY:
            return matchesArray(expected.asJsonArray(), actual.asJsonArray(), 0, new boolean[actual.asJsonArray().size()]);
        default:
            return expected.equals(actual);
        }
    }

    private static boolean matchesObject(final JsonObject expected, final JsonObject actual) {
        for (final Map.Entry<String, JsonValue> entry : expected.entrySet()) {
            if (!actual.containsKey(entry.getKey()) || !matches(entry.getValue(), actual.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchesArray(final JsonArray expected, final JsonArray actual, final int index,
            final boolean[] used) {
        if (expected.size() != actual.size()) {
            return false;
        }
        if (index == expected.size()) {
            return true;
        }
        for (int candidate = 0; candidate < actual.size(); candidate++) {
            if (!used[candidate] && matches(expected.get(index), actual.get(candidate))) {
                used[candidate] = true;
                if (matchesArray(expected, actual, index + 1, used)) {
                    return true;
                }
                used[candidate] = false;
            }
        }
        return false;
    }

    private static JsonValue parse(final String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readValue();
        }
    }
}
