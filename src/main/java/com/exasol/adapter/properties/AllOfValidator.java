package com.exasol.adapter.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates a property using multiple sub-validators.
 *
 * <p>
 * Executes all provided sub-validators sequentially. Collects and merges validation messages from any failing
 * sub-validators. Note that validators are executed in the order they are listed in the constructor.
 * </p>
 * <p>
 * Always completes all validations, regardless of intermediate failures.
 * </p>
 * <p>
 * If you need to combine validators and short-circuit please use {@link AndValidator} instead.
 * </p>
 */
public class AllOfValidator extends AbstractPropertyValidator {
    private final PropertyValidator[] validators;

    /**
     * Create a new instance of the {@link AllOfValidator}.
     *
     * @param context    validation context containing properties and validation logs
     * @param validators array of {@link PropertyValidator} instances to apply sequentially
     */
    AllOfValidator(final ValidationContext context, final PropertyValidator... validators) {
        super(context, "E-VSCOMJAVA-47");
        this.validators = validators;
    }

    /**
     * Executes all registered sub-validators, even if some validations fail.
     * 
     * @return any validation error message, each on a separate line in the original order the validations were executed
     */
    // [impl -> dsn~all-of-validation~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        final List<String> errorMessages = new ArrayList<>(this.validators.length);
        boolean isValid = true;
        for (final PropertyValidator validator : this.validators) {
            final ValidationResult result = validator.validate();
            if (!result.isValid()) {
                errorMessages.add(result.getMessage());
                isValid = false;
            }
        }
        return new ValidationResult(isValid, String.join(System.lineSeparator(), errorMessages));
    }
}
