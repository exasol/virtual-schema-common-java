package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates that all properties provided during schema creation are known.
 *
 * <p>
 * Ensures mandatory and optional properties are accounted for, preventing unrecognized or unused properties. Detects
 * potential issues like misspellings, outdated properties, or invalid property usage for the context.
 * </p>
 */
public class CoverageValidator extends AbstractPropertyValidator {

    /**
     * Create a new instance of {@link CoverageValidator}.
     *
     * @param context validation context containing properties and validation logs
     */
    CoverageValidator(final ValidationContext context) {
        super(context);
    }

    /**
     * Check that all properties provided by the users are recognized by at least one validator, including the optional
     * ones.
     * <p>
     * While mandatory properties have their own explicit checks, everything else is by definition an optional property.
     * We want to avoid that users misspell property names, use outdated properties or by mistake try to use properties
     * from a different dialect.
     * </p>
     * <p>
     * Please note that this does not indicate that all validations were successful. Only that the user provided no
     * property for which no validator exists.
     * </p>
     * 
     * @return success if all properties given by the virtual schema user were validated else indicate a failure and
     *         provide an error message
     */
    // [impl -> dsn~validation-completeness-check~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        final Set<String> unknownProperties = this.context.getProperties() //
                .keySet() //
                .stream() //
                .filter(propertyName -> !this.context.isKnownProperty(propertyName)) //
                .collect(Collectors.toSet());
        if (unknownProperties.isEmpty()) {
            return ValidationResult.success();
        } else {
            return new ValidationResult(false,
                    ExaError.messageBuilder("E-VSCOMJAVA-53")
                            .message("The following properties are unknown: {{properties}.",
                                    String.join("', '", unknownProperties))
                            .mitigation(
                                    "Please check the documentation of the adapter for valid properties and the spelling.")
                            .toString());
        }
    }
}
