package com.exasol.adapter.properties;

import com.exasol.errorreporting.ErrorMessageBuilder;
import com.exasol.errorreporting.ExaError;

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
    protected final ValidationContext context;
    protected final String propertyName;
    private final String errorCode;

    /**
     * Creates a new instance of {@code AbstractPropertyValidator}.
     *
     * <p>
     * Initializes the validator with the validation context, the property name to validate, and the error code to use
     * for reporting validation failures.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     * @param errorCode    error code to associate with validation failures
     */
    AbstractPropertyValidator(final ValidationContext context, final String propertyName, final String errorCode) {
        this.context = context;
        this.propertyName = propertyName;
        this.errorCode = errorCode;
    }

    /**
     * Create a new instance of {@code AbstractPropertyValidator} without a property name.
     *
     * @param context   validation context containing properties and validation logs
     * @param errorCode error code to associate with validation failures
     */
    AbstractPropertyValidator(final ValidationContext context, final String errorCode) {
        this(context, null, errorCode);
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
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

    /**
     * Create a new error message builder initialized with the error code of this validator.
     *
     * <p>
     * The returned builder should be used to construct detailed error messages associated with property validation
     * failures.
     * </p>
     *
     * @return ErrorMessageBuilder initialized with the validator's error code.
     */
    protected ErrorMessageBuilder createError() {
        return ExaError.messageBuilder(this.errorCode);
    }
}
