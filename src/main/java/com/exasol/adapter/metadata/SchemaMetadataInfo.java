package com.exasol.adapter.metadata;

import java.util.*;

/**
 * Represents the metadata of an EXASOL Virtual Schema which are sent with each request. The metadata are just "for
 * information" for the adapter. These metadata don't contain the table metadata.
 */
public class SchemaMetadataInfo {
    private final String schemaName;
    private final String adapterNotes;
    private final Map<String, String> properties;

    public SchemaMetadataInfo(final String schemaName, final String adapterNotes,
            final Map<String, String> properties) {
        this.schemaName = schemaName;
        this.adapterNotes = adapterNotes;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SchemaMetadataInfo.class.getSimpleName() + "{", "}")
                .add("schemaName=" + this.schemaName) //
                .add("adapterNotes=" + this.adapterNotes) //
                .add("properties=" + this.properties) //
                .toString();
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public String getAdapterNotes() {
        return this.adapterNotes;
    }

    /**
     * Keys are case-insensitive and stored upper case
     *
     * @return map with schema properties
     */
    public Map<String, String> getProperties() {
        return this.properties;
    }

    /**
     * Get a property value by the property name
     *
     * @param name property name
     * @return property value
     */
    public String getProperty(final String name) {
        return this.properties.get(name);
    }

    /**
     * Check if the property with the given key is set
     * 
     * @param key key to check
     * @return <code>true</code> if the property with the given key is set
     */
    public boolean containsProperty(final String key) {
        return this.properties.containsKey(key);
    }
}
