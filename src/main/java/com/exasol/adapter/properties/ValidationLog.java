package com.exasol.adapter.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs and tracks the properties that have been validated.
 */
public class ValidationLog {
    final List<String> validatedProperties = new ArrayList<>();

    /**
     * Create a new instance of {@link ValidationLog}.
     */
    public ValidationLog() {
    }

    /**
     * Register a property as validated.
     *
     * @param propertyName name of the property to mark as validated
     */
    public void addValidation(final String propertyName) {
        validatedProperties.add(propertyName);
    }

    /**
     * Check if a property has been validated.
     *
     * @param property name of the property to check
     *
     * @return true if the property has been validated, false otherwise
     */
    public boolean isValidated(final String property) {
        return validatedProperties.contains(property);
    }

    /**
     * Retrieve the list of properties that have been validated.
     *
     * @return list of validated property names
     */
    public List<String> getValidatedProperties() {
        return validatedProperties;
    }
}