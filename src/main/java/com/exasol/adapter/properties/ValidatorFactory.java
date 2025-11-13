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
     * @return validator for verifying a boolean property
     */
    public PropertyValidator bool(final String propertyName) {
        this.context.addKnownProperty(propertyName);
        return new BooleanValidator(this.context, propertyName);
    }

    /**
     * Create a validator to validate that a property's value is a valid integer.
     *
     * @param propertyName name of the property to validate
     * @return validator for validating an integer property
     */
    public PropertyValidator integer(final String propertyName) {
        this.context.addKnownProperty(propertyName);
        return new IntegerValidator(this.context, propertyName);
    }

    /**
     * Create a validator to validate that a property's value is a valid integer and optionally within a range.
     *
     * @param propertyName name of the property to validate
     * @param min          minimum allowable value (inclusive)
     * @param max          maximum allowable value (inclusive)
     * @return validator for validating an integer property with range constraints
     */
    public PropertyValidator integer(final String propertyName, final long min, final long max) {
        this.context.addKnownProperty(propertyName);
        return new IntegerValidator(this.context, propertyName, min, max);
    }

    /**
     * Creates a validator that checks whether the property matches a pattern
     *
     * @param propertyName name of the property to validate
     * @param pattern      regular expression pattern to match the property value against
     * @return validator that checks that a property
     */
    public StringValidator matches(final String propertyName, final Pattern pattern) {
        this.context.addKnownProperty(propertyName);
        return new StringValidator(this.context, propertyName, pattern);
    }

    /**
     * Create a validator that checks whether the value of the given property is a valid enum value.
     * <p>
     * Please note that due to type erasure in Java generics, we have logical duplication here between the type
     * parameter and the class of the enum. This is unfortunately unavoidable.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @param enumClass    enum to check against
     * @param <T>          enum type
     * @return validator for checking that the given value is contained in the enum
     */
    public <T extends Enum<T>> PropertyValidator enumeration(final String propertyName,
            final Class<T> enumClass) {
        this.context.addKnownProperty(propertyName);
        return new EnumerationValidator<>(this.context, propertyName, enumClass);
    }

    /**
     * Creates a validator for validating a multi-select property.
     * <p>
     * Please note that due to type erasure in Java generics, we have logical duplication here between the type
     * parameter and the class of the enum. This is unfortunately unavoidable. Empty values or values that are empty
     * after splitting at the commas are invalid.
     * </p>
     * 
     * @param propertyName name of the property to validate
     * @param enumClass    enum defining the allowed values
     * @param <T>          type of the enum defining the allowed values for the property
     * 
     * @return an instance of {@code MultiSelectValidator} for validating the property
     */
    public <T extends Enum<T>> PropertyValidator multiSelect(final String propertyName,
            final Class<T> enumClass) {
        return new MultiSelectValidator<>(this.context, propertyName, enumClass);
    }

    /**
     * Creates a validator for validating a multi-select property, allowing an empty value.
     * <p>
     * Please note that due to type erasure in Java generics, we have logical duplication here between the type
     * parameter and the class of the enum. This is unfortunately unavoidable.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @param enumClass    enum defining the allowed values
     * @param <T>          type of the enum defining the allowed values for the property
     *
     * @return an instance of {@code MultiSelectValidator} for validating the property
     */
    public <T extends Enum<T>> PropertyValidator multiSelectEmptyAllowed(final String propertyName,
            final Class<T> enumClass) {
        return new MultiSelectValidator<>(this.context, propertyName, enumClass, true);
    }

    /**
     * Create a validator for an Exasol object identifier property.
     *
     * <p>
     * Validates that the property value conforms to the format rules for Exasol database object identifiers.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @return validator for verifying an Exasol object identifier
     */
    public PropertyValidator exasolObjectId(final String propertyName) {
        this.context.addKnownProperty(propertyName);
        return new ExasolObjectIdValidator(this.context, propertyName);
    }

    /**
     * Create a validator to enforce the presence of a mandatory property.
     * <p>
     * Validates that the specified property exists and is not empty.
     * </p>
     *
     * @param propertyName name of the property to validate
     * @return validator for verifying the mandatory property
     */
    public PropertyValidator required(final String propertyName) {
        this.context.addKnownProperty(propertyName);
        return new RequiredValidator(this.context, propertyName);
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
     * @return a composite validator ensuring the property is required and satisfies the additional validation rules
     */
    public PropertyValidator required(final PropertyValidator validator) {
        this.context.addKnownProperty(validator.getPropertyName());
        return and(new RequiredValidator(this.context, validator.getPropertyName()),
                validator);
    }

    /**
     * Create a validator that makes sure an unwanted property is not set.
     *
     * @param propertyName name of the property to validate
     * @return validator for verifying the unwanted property is not set
     */
    public PropertyValidator unwanted(final String propertyName) {
        this.context.addKnownProperty(propertyName);
        return new RequiredValidator(this.context, propertyName, false);
    }

    /**
     * Create a validator to check if a property's value matches a regular expression pattern.
     *
     * @param propertyName      name of the property to validate
     * @param pattern           regular expression the property value must match
     * @param formatDescription description of the expected format
     * @return validator for validating a property against a regular expression
     */
    public StringValidator matches(final String propertyName, final Pattern pattern,
            final String formatDescription) {
        this.context.addKnownProperty(propertyName);
        return new StringValidator(this.context, propertyName, pattern, formatDescription);
    }

    /**
     * Create a validator to check if a property's value is a Unix path.
     *
     * @param propertyName name of the property to validate
     * @return validator checking that a property is a valid Unix path
     */
    public UnixPathValidator absolutePath(final String propertyName) {
        return new UnixPathValidator(this.context, propertyName);
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