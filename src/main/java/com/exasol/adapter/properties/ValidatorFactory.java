package com.exasol.adapter.properties;

import java.util.regex.Pattern;

/**
 * Factory for creating property validators.
 *
 * <p>
 * Provides methods to create validators for various types of properties and validation rules.
 * </p>
 */
// [impl -> dsn~validator-composition~1]
public class ValidatorFactory {
    private final ValidationContext context;

    /**
     * Create a new instance of {@link ValidatorFactory}.
     *
     * @param properties virtual schema properties to be validated
     *
     * @return instance of {@link ValidatorFactory}
     */
    public static ValidatorFactory create(final AdapterProperties properties) {
        return new ValidatorFactory(new ValidationContext(properties));
    }

    /**
     * Create a new instance of the {@link ValidatorFactory}.
     *
     * @param context validation context containing adapter properties and validation log
     */
    private ValidatorFactory(final ValidationContext context) {
        this.context = context;
    }

    /**
     * Create a validator for a boolean value.
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @return validator for verifying a boolean property
     */
    public PropertyValidator bool(final String propertyName, final String errorCode) {
        this.context.addKnownProperty(propertyName);
        return new BooleanValidator(this.context, propertyName, errorCode);
    }

    /**
     * Create a validator to validate that a property's value is a valid integer.
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @return validator for validating an integer property
     */
    public PropertyValidator integer(final String propertyName, final String errorCode) {
        this.context.addKnownProperty(propertyName);
        return new IntegerValidator(this.context, propertyName, errorCode);
    }

    /**
     * Create a validator to validate that a property's value is a valid integer and optionally within a range.
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @param min          minimum allowable value (inclusive)
     * @param max          maximum allowable value (inclusive)
     * @return validator for validating an integer property with range constraints
     */
    public PropertyValidator integer(final String propertyName, final String errorCode, final long min,
            final long max) {
        this.context.addKnownProperty(propertyName);
        return new IntegerValidator(this.context, propertyName, errorCode, min, max);
    }

    /**
     * Creates a validator that checks whether the property matches a pattern
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @param pattern      regular expression pattern to match the property value against
     * @return validator that checks that a property
     */
    public StringValidator matches(final String propertyName, final String errorCode, final Pattern pattern) {
        this.context.addKnownProperty(propertyName);
        return new StringValidator(this.context, propertyName, errorCode, pattern);
    }

    /**
     * Create a validator that checks whether the value of the given property is a valid enum value.
     * <p>
     * Please note that due to type erasure in Java generics, we have logical duplication here between the type
     * parameter and the class of the enum. This is unfortunately unavoidable.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @param enumClass    enum to check against
     * @return validator for checking that the given value is contained in the enum
     * @param <T> enum type
     */
    public <T extends Enum<T>> PropertyValidator enumeration(final String propertyName, final String errorCode,
            final Class<T> enumClass) {
        this.context.addKnownProperty(propertyName);
        return new EnumerationValidator<>(this.context, propertyName, errorCode, enumClass);
    }

    /**
     * Create a validator for an Exasol object identifier property.
     *
     * <p>
     * Validates that the property value conforms to the format rules for Exasol database object identifiers.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @return validator for verifying an Exasol object identifier
     */
    public PropertyValidator exasolObjectId(final String propertyName, final String errorCode) {
        this.context.addKnownProperty(propertyName);
        return new ExasolObjectIdValidator(this.context, propertyName, errorCode);
    }

    /**
     * Create a validator to enforce the presence of a mandatory property.
     * <p>
     * Validates that the specified property exists and is not empty.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @return validator for verifying the mandatory property
     */
    public PropertyValidator required(final String propertyName, final String errorCode) {
        this.context.addKnownProperty(propertyName);
        return new RequiredValidator(this.context, propertyName, errorCode);
    }

    /**
     * Create a composite validator that combines a {@link RequiredValidator} with the specified validator.
     *
     * <p>
     * This method acts as a shorthand for combining a {@link RequiredValidator} and the given validator using a
     * short-circuiting {@link AndValidator}. The composite validator ensures that:
     * </p>
     * <ul>
     * <li>The property is present and not empty (enforced by the {@link RequiredValidator}).</li>
     * <li>The property satisfies the additional conditions defined by the given validator.</li>
     * </ul>
     * <p>
     * The validation short-circuits and stops as soon as the {@link RequiredValidator} fails, ensuring efficiency.
     * </p>
     *
     * @param validator the validator to combine with the {@link RequiredValidator}
     * 
     * @return a composite validator ensuring the property is required and satisfies the additional validation rules
     */
    public PropertyValidator required(final PropertyValidator validator) {
        this.context.addKnownProperty(validator.getPropertyName());
        return and(new RequiredValidator(this.context, validator.getPropertyName(), validator.getErrorCode()),
                validator);
    }

    /**
     * Create a validator that makes sure an unwanted property is not set.
     *
     * @param propertyName name of the property to validate
     * @param errorCode    error code to report if validation fails
     * @return validator for verifying the unwanted property is not set
     */
    public PropertyValidator unwanted(final String propertyName, final String errorCode) {
        this.context.addKnownProperty(propertyName);
        return new RequiredValidator(this.context, propertyName, errorCode, false);
    }

    /**
     * Create a validator to check if a property's value matches a regular expression pattern.
     *
     * @param propertyName      name of the property to validate
     * @param errorCode         error code to report if validation fails
     * @param pattern           regular expression the property value must match
     * @param formatDescription description of the expected format
     * @return validator for validating a property against a regular expression
     */
    public StringValidator matches(final String propertyName, final String errorCode, final Pattern pattern,
            final String formatDescription) {
        this.context.addKnownProperty(propertyName);
        return new StringValidator(this.context, propertyName, errorCode, pattern, formatDescription);
    }

    /**
     * Create a validator that applies all given sub-validators sequentially.
     *
     * @param validators sub-validators that all must succeed
     * @return validator that executes sub-validators sequentially
     */
    public PropertyValidator allOf(final PropertyValidator... validators) {
        for (final PropertyValidator validator : validators) {
            this.context.addKnownProperty(validator.getPropertyName());
        }
        return new AllOfValidator(this.context, validators);
    }

    /**
     * Create a validator that applies multiple sub-validators sequentially.
     * <p>
     * The validation short-circuits after the first failed validation.
     * </p>
     *
     * @param validators sub-validators that all must succeed
     * @return validator that executes sub-validators sequentially
     */
    public PropertyValidator and(final PropertyValidator... validators) {
        for (final PropertyValidator validator : validators) {
            this.context.addKnownProperty(validator.getPropertyName());
        }
        return new AndValidator(this.context, validators);
    }

    /**
     * Create a validator for ensuring that all properties provided during schema creation are covered by a validator.
     * <p>
     * Validates that all mandatory and optional properties are considered, preventing unused or unexpected properties.
     * What this does not mean is that all validators really ran. Short-circuiting validators like the
     * {@link AndValidator} can skip validations.
     * </p>
     *
     * @return validator instance for checking the completeness of property validation
     */
    public PropertyValidator allCovered() {
        return new CoverageValidator(this.context);
    }
}