package com.exasol.adapter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAdapterProperties {
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
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        assertThat(properties.containsKey("expected"), equalTo(true));
    }

    @Test
    void testAssertContainsKeyFalse() {
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        assertThat(properties.containsKey("unexpected"), equalTo(false));
    }

    @Test
    void testIsEnabledTrue() {
        this.rawProperties.put("switch", "TRUE");
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(true));
    }
}