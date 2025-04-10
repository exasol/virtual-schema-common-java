package com.exasol.adapter.properties;

/**
 * Interface for validating properties.
 *
 * The {@code PropertyValidator} interface defines the contract for validating properties.
 * Implementations of this interface encapsulate the logic for verifying whether a property
 * meets specific criteria. The result of the validation process is encapsulated in a
 * {@link ValidationResult} object.
 */
public interface PropertyValidator {
    /**
     * Validates the property and returns the result of the validation.
     *
     * This method evaluates the value of a property to determine whether it adheres
     * to the expected criteria. Implementations of this method define the specific
     * validation logic. The outcome of the validation process is encapsulated in an
     * instance of {@link ValidationResult}, which includes details about
     * whether the validation was successful and any associated messages.
     *
     * @return an {@link ValidationResult} object representing the result of the validation,
     *         including whether the validation was successful and any accompanying message
     */
    public ValidationResult validate();
}