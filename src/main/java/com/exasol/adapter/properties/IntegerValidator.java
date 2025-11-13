package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

/**
 * Validates a property value as an integer number with optional boundaries.
 * <p>
 * Ensures the value is a valid integer and optionally checks if it's within specified upper and lower boundaries.
 * </p>
 */
public class IntegerValidator extends AbstractPropertyValidator {
    private final long lowerBound;
    private final long upperBound;
    private final boolean bounded;

    /**
     * Create a new instance of a {@link IntegerValidator} to validate integer property values.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     */
    public IntegerValidator(final ValidationContext context, final String propertyName) {
        super(context, propertyName);
        this.lowerBound = Long.MIN_VALUE;
        this.upperBound = Long.MAX_VALUE;
        this.bounded = false;
    }

    /**
     * Creat a new instance of a {@link IntegerValidator} with boundary constraints.
     * <p>
     * Initializes with a validation context, property name, error code, and upper and lower boundaries for the integer
     * value.
     * </p>
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     * @param lowerBound   minimum allowed value (inclusive)
     * @param upperBound   maximum allowed value (inclusive)
     */
    IntegerValidator(final ValidationContext context, final String propertyName, final long lowerBound,
            final long upperBound) {
        super(context, propertyName);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.bounded = true;
        if (upperBound < lowerBound) {
            throw new IllegalArgumentException(ExaError //
                    .messageBuilder("E-VSCOMJAVA-54")
                    .message(
                            "The upper bound ({{upper_bound}}) must be greater than or equal the lower bound ({{lower_bound}}) for property {{property}}.", //
                            upperBound, lowerBound, propertyName) //
                    .ticketMitigation() //
                    .toString());
        }
    }

    /**
     * Validates whether the property value is a valid integer and within specified boundaries if provided.
     *
     * @return Validation result indicating success if the value is valid, or failure with an error message otherwise.
     */
    // [impl -> dsn~validating-integer-properties~1]
    // [impl -> dsn~integer-interval-validation~1]
    @Override
    public ValidationResult performSpecificValidation() {
        try {
            final long numericValue = Long.parseLong(this.getValue());
            if (this.bounded && (numericValue < this.lowerBound || numericValue > this.upperBound)) {
                return new ValidationResult(false, ExaError.messageBuilder("E-VSCOMJAVA-46").message(
                        "The value for property {{property}} must be between {{lower_bound}} and {{upper_bound}, but was {{value}}.",
                        this.propertyName, this.lowerBound, this.upperBound, numericValue).toString());
            }
            return ValidationResult.success();
        } catch (final NumberFormatException exception) {
            return new ValidationResult(false,
                    ExaError.messageBuilder("E-VSCOMJAVA-47")
                            .message("The value {{value}} for property {{property}} is not a valid integer number.",
                                    this.getValue(), this.propertyName)
                            .toString());
        }
    }
}