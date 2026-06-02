package com.exasol.adapter;

import static java.util.Collections.*;

import java.util.*;

/**
 * Utilities for working with collections.
 */
public final class CollectionUtils {
    private CollectionUtils() {
        // prevent instantiation
    }

    /**
     * Create an unmodifiable copy of a list or return an empty list if the input is {@code null}.
     * List elements may be {@code null}.
     *
     * @param <T>    element type
     * @param values source list
     * @return unmodifiable list copy or empty list
     */
    public static <T> List<T> copyOfOrEmpty(final List<T> values) {
        return values == null ? emptyList() : unmodifiableList(new ArrayList<>(values));
    }

    /**
     * Create an unmodifiable copy of a map or return an empty map if the input is {@code null}.
     * Map keys and values may be {@code null}.
     * <p>
     * Preserves the iteration order of the input map.
     *
     * @param <K>    key type
     * @param <V>    value type
     * @param values source map
     * @return unmodifiable map copy or empty map
     */
    public static <K, V> Map<K, V> copyOfOrEmpty(final Map<K, V> values) {
        return values == null ? emptyMap() : unmodifiableMap(new LinkedHashMap<>(values));
    }
}
