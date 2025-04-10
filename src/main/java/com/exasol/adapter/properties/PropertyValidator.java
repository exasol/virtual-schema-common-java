package com.exasol.adapter.properties;

/**
 * Interface for validating properties.
 * <p>
 * The {@code PropertyValidator} interface defines the contract for validating properties.
 * Implementations of this interface encapsulate the logic for verifying whether a property
 * meets specific criteria. The result of the validation process is encapsulated in a
 * </p>
 * {@link ValidationResult} object.
 */
public interface PropertyValidator {
    /**
     * Validate the property and returns the result of the validation.
     * <p>
     * This method evaluates the value of a property to determine whether it adheres
     * to the expected criteria. Implementations of this method define the specific
     * validation logic. The outcome of the validation process is encapsulated in an
     * instance of {@link ValidationResult}, which includes details about
     * whether the validation was successful and any associated messages.
     * </p>
     * @return an {@link ValidationResult} object representing the result of the validation,
     *         including whether the validation was successful and any accompanying message
     */
    public ValidationResult validate();

    /**
     * Retrieve the name of the property being validated.
     *
     * @return name of the property as a string
     */
    public String getPropertyName();

    /**
     * Retrieve the error code associated with the property validation.
     *
     * @return string representing the error code
     */
    public String getErrorCode();
}