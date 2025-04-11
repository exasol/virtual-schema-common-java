package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.util.regex.Pattern;

/**
 * Validates a property value as a boolean string.
 *
 * <p>
 * Ensures the value is either "true" or "false" (case-insensitive).
 */
public class BooleanValidator extends AbstractPropertyValidator {
    private static final Pattern BOOLEAN_REGEX = Pattern.compile("true|false", Pattern.CASE_INSENSITIVE);

    /**
     * Constructs a {@code BooleanValidator} to validate boolean property values.
     *
     * <p>
     * Initializes with a validation context, property name, and error code.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     */
    BooleanValidator(final ValidationContext context, final String propertyName) {
        super(context, propertyName);
    }

    /**
     * Validates whether the property value is a valid boolean string.
     *
     * <p>
     * The value must match the pattern "true" or "false" (case-insensitive).
     *
     * @return Validation result indicating success if the value is valid, or failure with an error message otherwise.
     */
    // [impl -> dsn~validating-boolean-properties~1]
    @Override
    public ValidationResult performSpecificValidation() {
        final String value = this.getValue();
        if ((value != null) && BOOLEAN_REGEX.matcher(value).matches())
            return ValidationResult.success();
        else {
            return new ValidationResult(false,
                    ExaError.messageBuilder("E-VSCOMJAVA-43")
                            .message("The value {{value}} for property {{property}} must be either 'true' or 'false'.",
                                    value, this.propertyName)
                            .toString());
        }
    }
}
