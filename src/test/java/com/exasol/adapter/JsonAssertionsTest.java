package com.exasol.adapter;

import static com.exasol.adapter.JsonAssertions.assertJsonMatches;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class JsonAssertionsTest {
    @Test
    void matchesNestedObjectsAndUnorderedArrays() {
        assertJsonMatches("{\"items\":[{\"id\":1},{\"id\":2}]}",
                "{\"items\":[{\"id\":2,\"extra\":true},{\"id\":1}],\"other\":null}");
    }

    @Test
    void matchesNumbersWithDifferentScales() {
        assertJsonMatches("{\"value\":1}", "{\"value\":1.0}");
    }

    @Test
    void rejectsReusingAnArrayElement() {
        assertThrows(AssertionFailedError.class,
                () -> assertJsonMatches("[{\"id\":1},{\"id\":1}]", "[{\"id\":1},{\"id\":2}]"));
    }
}
