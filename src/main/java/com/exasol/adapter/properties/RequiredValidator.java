package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

/**
 * Validator for ensuring that a required property is set and not empty.
 */
public class RequiredValidator extends AbstractPropertyValidator {
    private final boolean required;

    /**
     * Create a new instance of a {@link RequiredValidator}
     *
     * @param context      validation context with properties and validation log
     * @param propertyName name of the property to be validated
     * @param required  {@code true} the property need to be set, {@code false} otherwise
     */
    public RequiredValidator(final ValidationContext context, final String propertyName, final boolean required) {
        super(context, propertyName);
        this.required = required;
    }

    /**
     * Create a new instance of a {@link RequiredValidator}
     *
     * @param context      validation context with properties and validation log
     * @param propertyName name of the property to be validated
     */
    RequiredValidator(final ValidationContext context, final String propertyName) {
        this(context, propertyName, true);
    }

    /**
     * Verify that the property is set as required.
     * <p>
     * A property counts as set if it exists (i.e. the value is not {@code null}) and not an empty string.
     * </p>
     * 
     * @return success in case the property is set, failed validation with an error message indicating the issue
     *         otherwise
     */
    @Override
    public ValidationResult performSpecificValidation() {
        if (this.required) {
            return validatePropertyIsSet();
        } else {
            return validatePropertyIsNotSet();
        }
    }

    // [impl -> dsn~validating-the-existence-of-mandatory-properties~1]
    private ValidationResult validatePropertyIsSet() {
        if (this.getValue() == null) {
            return new ValidationResult(false, ExaError.messageBuilder("E-VSCOMJAVA-48")
                    .message("The mandatory property {{property}} is missing.", this.propertyName).toString());
        } else if (this.getValue().isEmpty()) {
            return new ValidationResult(false, ExaError.messageBuilder("E-VSCOMJAVA-49")
                    .message("The mandatory property {{property}} is empty.", this.propertyName).toString());
        } else {
            return ValidationResult.success();
        }
    }

    // [impl -> dsn~validating-the-absence-of-unwanted-properties~1]
    private ValidationResult validatePropertyIsNotSet() {
        if (this.getValue() == null || this.getValue().isEmpty()) {
            return ValidationResult.success();
        } else {
            return new ValidationResult(false,
                    ExaError.messageBuilder("E-VSCOMJAVA-50")
                            .message("The unwanted property {{property}} is set.", this.propertyName)
                            .mitigation("Please remove the property.").toString());
        }
    }
}