package com.exasol.adapter;

import java.util.*;

/**
 * This class provides access to the user defined adapter properties.
 */
public class AdapterProperties {
    private final Map<String, String> properties;

    /**
     * Create a new instance of {@link AdapterProperties} from a key-value map
     *
     * @param properties map of property keys and values
     */
    public AdapterProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Get empty map
     *
     * @return empty map
     */
    public static Map<String, String> emptyProperties() {
        return Collections.emptyMap();
    }

    /**
     * Check if the properties are empty
     *
     * @return <code>true</code> if there are no properties
     */
    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    /**
     * Check if a property with the given key exists
     *
     * @param key property to search for
     * @return <code>true</code> if the property list contains the property with the given key
     */
    public boolean containsKey(final String key) {
        return this.properties.containsKey(key);
    }

    /**
     * Check if the switch with the given key is enabled
     *
     * @param key switch name
     * @return <code>true</code> if the switch property exists and is enable
     */
    public boolean isEnabled(final String key) {
        return "true".equalsIgnoreCase(this.properties.get(key));
    }

    /**
     * Get value for key
     *
     * @param key property to search for
     * @return corresponding value or null it the key-value pair does not exist
     */
    public String get(final String key) {
        return this.properties.get(key);
    }

    /**
     * Get set of contained keys
     *
     * @return a set view of the keys contained in properties
     */
    public Set<String> keySet() {
        return this.properties.keySet();
    }

    /**
     * Get collection of contained values
     *
     * @return a collection view of the values contained in properties
     */
    public Collection<String> values() {
        return this.properties.values();
    }

    /**
     * Get set of contained entries
     *
     * @return a Set view of the mappings contained in properties
     */
    public Set<Map.Entry<String, String>> entrySet() {
        return this.properties.entrySet();
    }
}