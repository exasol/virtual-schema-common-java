package com.exasol.adapter.metadata;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Represents the metadata of an EXASOL Virtual Schema which are sent with each request. The metadata are just "for
 * information" for the adapter. These metadata don't contain the table metadata.
 */
public class SchemaMetadataInfo {
    private final String schemaName;
    private final String adapterNotes;
    private final Map<String, String> properties;

    /**
     * Instantiates a new Schema metadata info.
     *
     * @param schemaName   the schema name
     * @param adapterNotes the adapter notes
     * @param properties   the properties
     */
    public SchemaMetadataInfo(final String schemaName, final String adapterNotes,
            final Map<String, String> properties) {
        this.schemaName = schemaName;
        this.adapterNotes = adapterNotes;
        this.properties = properties;
    }

    /**
     * Gets schema name.
     *
     * @return the schema name
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    /**
     * Gets adapter notes.
     *
     * @return the adapter notes
     */
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

    @Override
    public String toString() {
        return new StringJoiner(", ", SchemaMetadataInfo.class.getSimpleName() + "{", "}")
                .add("schemaName=" + this.schemaName) //
                .add("adapterNotes=" + this.adapterNotes) //
                .add("properties=" + this.properties) //
                .toString();
    }
}
