package com.exasol.adapter.properties;

/**
 * Validator that combines multiple validators using a logical AND operation.
 *
 * <p>
 * Short-circuits after the first failed validation. Passes validation only if all composed validators succeed.
 * </p>
 */
public class AndValidator extends AbstractPropertyValidator {
    private final PropertyValidator[] validators;

    /**
     * Constructs an {@code AndValidator} combining multiple property validators.
     *
     * @param context    validation context encapsulating properties and logs
     * @param validators array of {@code PropertyValidator} instances to combine using logical AND
     */
    AndValidator(final ValidationContext context, final PropertyValidator... validators) {
        super(context);
        this.validators = validators;
    }

    /**
     * Performs validation using all assigned validators.
     *
     * <p>
     * Iterates through all validators, applying each validation. Returns the first failed validation result or a
     * successful result if all validations pass.
     *
     * @return result of the first failed validation or a successful result if all validators pass
     */
    // [impl -> dsn~short-circuiting-and-validation~1]
    public ValidationResult performSpecificValidation() {
        for (PropertyValidator validator : this.validators) {
            final ValidationResult result = validator.validate();
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.success();
    }
}
