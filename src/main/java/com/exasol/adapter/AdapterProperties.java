package com.exasol.adapter;

import java.util.Map;

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
}