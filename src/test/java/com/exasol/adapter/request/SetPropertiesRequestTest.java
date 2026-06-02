package com.exasol.adapter.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

class SetPropertiesRequestTest {
    @Test
    void testCopiesPropertiesDefensively() {
        final Map<String, String> properties = new HashMap<>();
        properties.put("A", "B");
        final SetPropertiesRequest request = new SetPropertiesRequest(null, properties);
        properties.put("A", "CHANGED");
        assertThat(request.getProperties().get("A"), equalTo("B"));
    }

    @Test
    void testGetPropertiesReturnsUnmodifiableMap() {
        final SetPropertiesRequest request = new SetPropertiesRequest(null, Map.of("A", "B"));
        final Map<String, String> properties = request.getProperties();
        assertThrows(UnsupportedOperationException.class, () -> properties.put("A", "CHANGED"));
    }

    @Test
    void testTreatsNullPropertiesAsEmptyMap() {
        assertThat(new SetPropertiesRequest(null, null).getProperties(), anEmptyMap());
    }

    @Test
    void testGetPropertiesPreservesOrder() {
        final Map<String, String> properties = new LinkedHashMap<>();
        properties.put("B", "2");
        properties.put("A", "1");
        properties.put("C", "3");
        final SetPropertiesRequest request = new SetPropertiesRequest(null, properties);
        assertThat(request.getProperties().keySet(), contains("B", "A", "C"));
    }
}
