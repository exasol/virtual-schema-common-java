package com.exasol.adapter.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

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
}
