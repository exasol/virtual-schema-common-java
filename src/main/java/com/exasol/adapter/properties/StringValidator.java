package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.util.regex.Pattern;

/**
 * Validator that matches a string property against a regular expression pattern.
 */
public class StringValidator extends AbstractPropertyValidator {
    private final Pattern pattern;
    private final String formatDescription;

    /**
     * Create a new instance of {@link StringValidator}.
     *
     * @param context           validation context containing properties and validation logs
     * @param propertyName      name of the property to validate
     * @param pattern           regular expression the property value must match
     * @param formatDescription description of the expected format to include in error messages
     */
    StringValidator(final ValidationContext context, final String propertyName,
            final Pattern pattern, final String formatDescription) {
        super(context, propertyName);
        this.pattern = pattern;
        this.formatDescription = formatDescription;
    }

    /**
     * Create a new instance of {@link StringValidator}.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     * @param pattern      regular expression the property value must match
     */
    StringValidator(final ValidationContext context, final String propertyName, final Pattern pattern) {
        this(context, propertyName, pattern, null);
    }

    /**
     * Validate the property value against the configured regular expression pattern.
     *
     * <p>
     * If the value matches the pattern, the validation succeeds. If it does not match, the validation fails and
     * provides an error message.
     * </p>
     * <p>
     * If a format description is given during construction, the error message will use that, otherwise it will display
     * the regular expression.
     * </p>
     *
     * @return validation result containing success or failure status and an associated message
     */
    // [impl -> dsn~validating-a-string-against-a-regular-expression~1]
    // [impl -> dsn~reporting-format-violations-in-properties~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        if (this.pattern.matcher(this.getValue()).matches()) {
            return ValidationResult.success();
        } else {
            if (this.formatDescription == null) {
                return new ValidationResult(false,
                        ExaError.messageBuilder("E-VSCOMJAVA-51")
                                .message("The property {{property}} has an invalid value {{value}}.",
                                        this.propertyName, this.getValue())
                                .mitigation("Please use a value matching the regular expression '{{pattern}}'.",
                                        this.pattern)
                                .toString());
            } else {
                return new ValidationResult(false,
                        ExaError.messageBuilder("E-VSCOMJAVA-55")
                                .message("The property {{property}} has an invalid value {{value}}.",
                                        this.propertyName, this.getValue())
                                .mitigation("Please use the format {{format}}.", this.formatDescription).toString());
            }
        }
    }
}