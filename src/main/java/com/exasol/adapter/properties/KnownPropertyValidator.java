package com.exasol.adapter.properties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates that all properties in a context are among a set of known properties.
 *
 * <p>
 * Ensures that no unexpected or unknown properties are defined in the validation context.
 * </p>
 */
public class KnownPropertyValidator extends AbstractPropertyValidator {
    private final List<String> propertyNames;

    /**
     * Create a new instance of {@link KnownPropertyValidator}.
     *
     * <p>
     * Validates that all properties in a given validation context are among a predefined list of known property names.
     * </p>
     * 
     * @param context       validation context containing properties and validation logs
     * @param errorCode     error code to report validation failures
     * @param propertyNames list of property names considered valid for the context
     */
    public KnownPropertyValidator(final ValidationContext context, final String errorCode,
                                  final List<String> propertyNames) {
        super(context, null, errorCode);
        this.propertyNames = propertyNames;
    }

    /**
     * Validate properties in the current context against predefined known property names.
     *
     * <p>
     * Checks for any unknown properties in the context and returns a result indicating success or failure.
     * </p>
     *
     * <p>
     * If the validation fails due to the presence of unknown properties, an error message will be included in the
     * result.
     * </p>
     *
     * @return validation result denoting success if all properties are valid or failure if unknown properties exist
     */
    @Override
    public ValidationResult performSpecificValidation() {
        final List<String> unknownProperties = this.context.getProperties().keySet().stream()
                .filter(key -> !this.propertyNames.contains(key)).collect(Collectors.toList());
        if (!unknownProperties.isEmpty()) {
            return new ValidationResult(false,
                    createError().message("Unknown adapter properties specified: {{unknownProperties}}.",
                            String.join("', '", unknownProperties)).toString());
        } else
            return ValidationResult.success();
    }
}