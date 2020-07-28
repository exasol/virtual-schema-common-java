package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractAdapterPropertiesTest {
    private Map<String, String> rawProperties;

    @BeforeEach
    void beforeEach() {
        this.rawProperties = new HashMap<>();
    }

    @Test
    void testPropertiesEmptyByDefault() {
        assertThat(new AdapterProperties(this.rawProperties).isEmpty(), equalTo(true));
    }

    @Test
    void testAssertContainsKeyTrue() {
        this.rawProperties.put("expected", null);
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.containsKey("expected"), equalTo(true));
    }

    @Test
    void testAssertContainsKeyFalse() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.containsKey("unexpected"), equalTo(false));
    }

    @Test
    void testIsEnabledTrue() {
        this.rawProperties.put("switch", "TRUE");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(true));
    }

    @Test
    void testIsEnabledFalseIfPropertyDoesNotExist() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(false));
    }

    @Test
    void testIsEnabledFalseIfPropertyIsNull() {
        this.rawProperties.put("switch", null);
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(false));
    }

    @Test
    void testIsEnabledFalseIfPropertyIsNotTrue() {
        this.rawProperties.put("switch", "false");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(false));
    }

    @Test
    void testGetReturnsNull() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.get("key"), nullValue());
    }

    @Test
    void testGetReturnsValue() {
        this.rawProperties.put("key", "value");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.get("key"), equalTo("value"));
    }

    @Test
    void testKeySet() {
        this.rawProperties.put("key1", "value1");
        this.rawProperties.put("key2", "value2");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.keySet(), containsInAnyOrder("key1", "key2"));
    }

    @Test
    void testKeySetEmpty() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertTrue(properties.keySet().isEmpty());
    }

    @Test
    void testValues() {
        this.rawProperties.put("key1", "value1");
        this.rawProperties.put("key2", "value2");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.values(), containsInAnyOrder("value1", "value2"));
    }

    @Test
    void testValuesEmpty() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertTrue(properties.values().isEmpty());
    }

    @Test
    void testEntrySet() {
        this.rawProperties.put("key1", "value1");
        this.rawProperties.put("key2", "value2");
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        final Set<Map.Entry<String, String>> entriesSet = properties.entrySet();
        assertThat(entriesSet.size(), equalTo(2));
    }

    private static class DummyAdapterProperties extends AbstractAdapterProperties {
        public DummyAdapterProperties(final Map<String, String> properties) {
            super(properties);
        }
    }
}