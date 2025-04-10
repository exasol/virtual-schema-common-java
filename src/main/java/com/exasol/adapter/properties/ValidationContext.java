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
    private final ValidationLog validationLog;
    private final Set<String> knownProperties = new HashSet<>();

    /**
     * Create a new instance of {@link ValidationContext}.
     *
     * @param properties    adapter properties for validation
     * @param validationLog log to track validated properties
     * @param knownProperties properties the adapter knows.
     */
    public ValidationContext(final AdapterProperties properties, final ValidationLog validationLog,
            final Set<String> knownProperties) {
        this.properties = properties;
        this.validationLog = validationLog;
        this.knownProperties.addAll(knownProperties);
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
     * Retrieve the validation log that tracks validated properties.
     *
     * @return validation log
     */
    public ValidationLog getValidationLog() {
        return this.validationLog;
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
     * Add multiple known property names to the list of known properties in the validation context.
     *
     * <p>
     * This method enables expanding the list of property names recognized in the validation process, which is for
     * example required if a virtual schema dialect introduces extra properties on top of the ones every dialect
     * supports.
     * </p>
     *
     * @param knownProperties array of property names to add to the known properties list
     */
    public void addKnownProperties(final String... knownProperties) {
        for (final String knownProperty : knownProperties) {
            this.knownProperties.add(knownProperty);
        }
    }
}