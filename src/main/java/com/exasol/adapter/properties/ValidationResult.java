package com.exasol.adapter.properties;

/**
 * Represents the result of a property validation.
 * <p>
 * This class is used to encapsulate the outcome of a property validation process, which includes
 * whether the validation was successful and an associated message describing the result.
 * </p>
 */
public class ValidationResult {
    private final boolean valid;
    private final String message;

    /**
     * Creates a new instance of {@code PropertyValidationResult}.
     *
     * @param valid   indicates whether the property validation was successful
     * @param message message providing additional information about the validation result
     */
    public ValidationResult(boolean valid, final String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Convenience constructor for a result representing a successful validation.
     *
     * @return successful validation result
     */
    public static ValidationResult success() {
        return new ValidationResult(true, "");
    }

    /**
     * Checks whether the property validation was successful.
     *
     * @return {code true} if the property validation was successful; {@code false} otherwise
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Retrieves the validation message associated with this result.
     *
     * @return message providing additional information about the validation result
     */
    public String getMessage() {
        return message;
    }
}