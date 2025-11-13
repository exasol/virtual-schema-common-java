package com.exasol.adapter.properties;

import java.util.*;

/**
 * Encapsulates properties and validation logs for property validation.
 *
 * <p>
 * Provides context required during validation, including access to adapter properties and a log for tracking validated
 * properties. The context also holds a list of known properties, which helps the validators find properties that are
 * misspelled or generally not allowed in a virtual schema dialect.
 * </p>
 */
public class ValidationContext {
    private final AdapterProperties properties;
    private final Set<String> knownProperties = new HashSet<>();

    /**
     * Create a new instance of {@link ValidationContext}.
     *
     * @param properties adapter properties for validation
     */
    public ValidationContext(final AdapterProperties properties) {
        this.properties = properties;
    }

    /**
     * Get the adapter properties.
     *
     * @return adapter properties
     */
    public AdapterProperties getProperties() {
        return this.properties;
    }

    /**
     * Retrieve the set of property names that the virtual schema adapter knows.
     *
     * @return set of known property names
     */
    public Set<String> getKnownProperties() {
        return this.knownProperties;
    }

    /**
     * Add a single property name to the known properties list.
     *
     * <p>
     * Expands the list of property names recognized in the validation context. Use for adding a single property.
     * </p>
     *
     * @param knownProperties name of the property to be added
     */
    public void addKnownProperty(final String knownProperties) {
        this.knownProperties.add(knownProperties);
    }

    /**
     * Check if a property name exists in the list of known properties.
     *
     * @param propertyName name of the property to check
     *
     * @return true if the property name is in the known properties list, false otherwise
     */
    public boolean isKnownProperty(final String propertyName) {
        return this.knownProperties.contains(propertyName);
    }
}