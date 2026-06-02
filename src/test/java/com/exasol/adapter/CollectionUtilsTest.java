package com.exasol.adapter;

import static com.exasol.adapter.CollectionUtils.copyOfOrEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CollectionUtilsTest {
    @Test
    void testCopyListOfOrEmptyWithNull() {
        assertThat(copyOfOrEmpty((List<String>) null), empty());
    }

    @Test
    void testCopyListOfOrEmptyCreatesImmutableCopy() {
        final List<String> source = new ArrayList<>(List.of("a", "b"));
        final List<String> copy = copyOfOrEmpty(source);
        source.add("c");
        assertAll(() -> assertThat(copy, equalTo(List.of("a", "b"))),
                () -> assertThrows(UnsupportedOperationException.class, () -> copy.add("c")));
    }

    @Test
    void testCopyListOfOrEmptyPreservesOrder() {
        assertThat(copyOfOrEmpty(List.of("b", "a", "c")), contains("b", "a", "c"));
    }

    @Test
    void testCopyListOfOrEmptyWithNullElement() {
        final List<String> source = new ArrayList<>();
        source.add("a");
        source.add(null);
        assertThat(copyOfOrEmpty(source), contains("a", (String) null));
    }

    @Test
    void testCopyMapOfOrEmptyWithNull() {
        assertThat(copyOfOrEmpty((Map<String, String>) null), equalTo(Map.of()));
    }

    @Test
    void testCopyMapOfOrEmptyCreatesImmutableCopy() {
        final Map<String, String> source = new LinkedHashMap<>();
        source.put("a", "b");
        final Map<String, String> copy = copyOfOrEmpty(source);
        source.put("a", "c");
        assertAll(() -> assertThat(copy, equalTo(Map.of("a", "b"))),
                () -> assertThrows(UnsupportedOperationException.class, () -> copy.put("a", "c")));
    }

    @Test
    void testCopyMapOfOrEmptyPreservesOrder() {
        final Map<String, String> source = new LinkedHashMap<>();
        source.put("b", "2");
        source.put("a", "1");
        source.put("c", "3");
        final Map<String, String> copy = copyOfOrEmpty(source);
        assertThat(new ArrayList<>(copy.keySet()), contains("b", "a", "c"));
    }

    @ParameterizedTest
    @CsvSource(value = { "a,<null>", "<null>,a", "<null>,<null>", "a,b" }, nullValues = "<null>")
    void testCopyMapOfOrEmptyWithNullKeyOrValue(final String key, final String value) {
        final Map<String, String> source = new LinkedHashMap<>();
        source.put(key, value);
        assertThat(copyOfOrEmpty(source), equalTo(source));
    }
}
