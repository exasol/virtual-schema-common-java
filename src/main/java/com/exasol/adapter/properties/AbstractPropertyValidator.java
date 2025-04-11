package com.exasol.adapter.properties;

/**
 * Base class for implementing property validators.
 *
 * <p>
 * Provides common functionality for validating a specific property, including accessing the property value, defining
 * error handling, and logging validation actions.
 *
 * <p>
 * Subclasses must implement {@link #performSpecificValidation()} to define property-specific validation logic.
 */
public abstract class AbstractPropertyValidator implements PropertyValidator {
    /** The context for the validation with the adapter properties and a log of covered properties */
    protected final ValidationContext context;
    /** The name of the property to be validated. */
    protected final String propertyName;

    /**
     * Creates a new instance of {@code AbstractPropertyValidator}.
     *
     * <p>
     * Initializes the validator with the validation context, the property name to validate, and the error code to use
     * for reporting validation failures.
     * </p>
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     */
    AbstractPropertyValidator(final ValidationContext context, final String propertyName) {
        this.context = context;
        this.propertyName = propertyName;
    }

    /**
     * Create a new instance of {@code AbstractPropertyValidator} without a property name.
     * <p>
     * This is useful for composite validators which are responsible for validation beyond a single property.
     * </p>
     *
     * @param context   validation context containing properties and validation logs
     */
    AbstractPropertyValidator(final ValidationContext context) {
        this(context, null);
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Retrieve the value of the property associated with the current validator.
     *
     * @return value of the property or {@code null} if the property is not set
     */
    protected String getValue() {
        return this.context.getProperties().get(this.propertyName);
    }

    /**
     * Validate the associated property and returns the validation result.
     *
     * <p>
     * Records the property name in the validation log before performing the specific validation defined by subclasses.
     * </p>
     *
     * @return validation result indicating whether the property is valid, along with an associated message if
     *         validation fails.
     */
    public final ValidationResult validate() {
        return performSpecificValidation();
    }

    /**
     * Validate the specific property according to custom logic implemented by subclasses.
     *
     * <p>
     * Performed as part of the overall property validation process. This method defines how the property value is
     * checked against specific conditions.
     * </p>
     *
     * @return result of the validation, indicating validity and an associated message if invalid.
     */
    protected abstract ValidationResult performSpecificValidation();

}
