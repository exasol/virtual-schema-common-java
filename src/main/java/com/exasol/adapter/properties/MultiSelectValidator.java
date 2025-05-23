package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.util.ArrayList;
import java.util.List;

/**
 * Property validator that validates that the value of a give property is a sub-set of the values of an enumeration.
 *
 * @param <T> enumeration that the values are checked against
 */
public class MultiSelectValidator<T extends Enum<T>> extends EnumerationValidator<T> {
    // The following regular expression intentionally limits the number of allowed spaces to avoid RegEx DOS.
    private static final String COMMA_SPLIT_REGEX = "\\s{0,10},\\s{0,10}";
    private final boolean emptyAllowed;

    /**
     * Constructs a new instance of {@code MultiSelectValidator}.
     * <p>
     * This validator is used for validating properties whose values can be selected from an enumeration of predefined
     * choices.
     * </p>
     * 
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     * @param enumClass    {@code Class} object corresponding to the enumeration type from which valid values can be
     *                     selected
     * @param emptyAllowed controls if empty values or values that are split at the commas are allowed to be empty.
     */
    public MultiSelectValidator(final ValidationContext context, final String propertyName,
            final Class<T> enumClass, final boolean emptyAllowed) {
        super(context, propertyName, enumClass);
        this.emptyAllowed = emptyAllowed;
    }

    /**
     * Constructs a new instance of {@code MultiSelectValidator}.
     * <p>
     * This validator is used for validating properties whose values can be selected from an enumeration of predefined
     * choices. Empty values are not allowed.
     * </p>
     * 
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     * @param enumClass    {@code Class} object corresponding to the enumeration type from which valid values can be
     *                     selected
     */
    MultiSelectValidator(final ValidationContext context, final String propertyName, final Class<T> enumClass) {
        this(context, propertyName, enumClass, false);
    }

    // [impl -> dsn~validating-multi-select-properties~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        if (this.getValue() == null) {
            return validateNull();
        } else {
            return validateNotNull();
        }
    }

    private ValidationResult validateNull() {
        return validateEmptyInput();
    }

    private ValidationResult validateEmptyInput() {
        return this.emptyAllowed
                ? ValidationResult.success()
                : createEmptyValueFailureResult();
    }

    private ValidationResult createEmptyValueFailureResult() {
        return ValidationResult.failure(ExaError.messageBuilder("E-VSCOMJAVA-56")
                .message("The property {{property}} must have at least one value set.", this.propertyName)
                .mitigation("Please select at least one of the following values: {{values}}."
                        + " Separate the individual values with a comma.",
                        String.join("', '", this.enumValueCache))
                .toString());
    }

    private ValidationResult validateNotNull() {
        if (this.getValue().isBlank()) {
            return validateEmptyInput();
        } else {
            final String[] givenValues = this.getValue().trim().split(COMMA_SPLIT_REGEX);
            if (givenValues.length == 0) {
                return validateEmptyInput();
            } else {
                return validateNonEmptyValueList(givenValues);
            }
        }
    }

    private ValidationResult validateNonEmptyValueList(final String[] givenValues) {
        final List<String> unknownValues = new ArrayList<>();
        for (final String givenValue : givenValues) {
            if (!this.enumValueCache.contains(givenValue)) {
                unknownValues.add(givenValue);
            }
        }
        return unknownValues.isEmpty()
                ? ValidationResult.success()
                : ValidationResult.failure(ExaError.messageBuilder("E-VSCOMJAVA-57")
                .message(
                        "The following values given for the property {{property}} are unknown: {{unknown}}",
                        this.propertyName, String.join("', '", unknownValues))
                .mitigation("Please use one or more of the following values: {{values}}."
                                + " Separate the individual values with a comma.",
                        String.join("', '", this.enumValueCache))
                .toString());
    }
}