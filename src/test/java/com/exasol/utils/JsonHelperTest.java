package com.exasol.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JsonHelperTest {
    private static final String DEFAULT_VALUE = "default";
    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String JSON = "\n{\n    \"type\":\"getCapabilities\",\n    \"capabilities\":\"LIMIT\"\n}";
    @Mock
    private JsonObject jsonObject;
    @Mock
    private JsonValue jsonValue;
    private JsonObject expectedJsonObject;

    @BeforeEach
    void setUp() {
        when(this.jsonObject.containsKey(KEY_1)).thenReturn(false);
        when(this.jsonObject.containsKey(KEY_2)).thenReturn(true);
        when(this.jsonValue.toString()).thenReturn(KEY_2);
        when(this.jsonObject.get(KEY_2)).thenReturn(this.jsonValue);
        this.expectedJsonObject =
              Json.createObjectBuilder().add("type", "getCapabilities").add("capabilities", "LIMIT").build();
    }

    @Test
    void testGetJsonObject() {
        assertThat(JsonHelper.getJsonObject(JSON), equalTo(this.expectedJsonObject));
    }

    @Test
    void testGetKeyAsStringWithDefaultValue() {
        assertThat(JsonHelper.getKeyAsString(this.jsonObject, KEY_1, DEFAULT_VALUE), equalTo(DEFAULT_VALUE));
    }

    @Test
    void testGetKeyAsString() {
        assertThat(JsonHelper.getKeyAsString(this.jsonObject, KEY_2, DEFAULT_VALUE), equalTo(KEY_2));
    }

    @Test
    void testPrettyJson() {
        assertThat(JsonHelper.prettyJson(this.expectedJsonObject), equalTo(JSON));
    }
}