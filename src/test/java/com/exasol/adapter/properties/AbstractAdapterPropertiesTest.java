package com.exasol.adapter.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    void testIsEnabledFalseIfPropertyDoesNotExist() {
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(false));
    }

    private static Stream<Arguments> provideIsEnabledTestCases() {
        return Stream.of(
                Arguments.of("TRUE", true),
                Arguments.of(null, false),
                Arguments.of("false", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIsEnabledTestCases")
    void testIsEnabled(final String propertyValue, final boolean expectedResult) {
        this.rawProperties.put("switch", propertyValue);
        final DummyAdapterProperties properties = new DummyAdapterProperties(this.rawProperties);
        assertThat(properties.isEnabled("switch"), equalTo(expectedResult));
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