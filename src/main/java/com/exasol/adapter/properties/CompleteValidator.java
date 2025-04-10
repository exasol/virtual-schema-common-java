package com.exasol.adapter.properties;

import java.util.HashSet;
import java.util.Set;

/**
 * Validates that all properties provided during schema creation are properly validated.
 *
 * <p>
 * Ensures mandatory and optional properties are accounted for, preventing unrecognized or unused properties. Detects
 * potential issues like misspellings, outdated properties, or invalid property usage for the context.
 */
public class CompleteValidator extends AbstractPropertyValidator {
    /**
     * Create a new instance of {@link CompleteValidator}.
     *
     * @param context validation context containing properties and validation logs
     */
    CompleteValidator(final ValidationContext context) {
        super(context, "E-VSCOMJAVA-46");
    }

    /**
     * Check that all properties provided by the users were validated, including the optional ones.
     * <p>
     * While mandatory properties have their own explicit checks, everything else is by definition an optional property.
     * We want to avoid that users misspell property names, use outdated properties or by mistake try to use properties
     * from a different dialect.
     * </p>
     * 
     * @return success if all properties given by the virtual schema user were validated else indicate a failure and
     *         provide an error message
     */
    // [impl -> dsn~validation-completeness-check~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        final Set<String> properties = new HashSet<>(context.getProperties().keySet());
        properties.removeAll(context.getValidationLog().validatedProperties);
        if (properties.isEmpty()) {
            return ValidationResult.success();
        } else {
            return new ValidationResult(false, createError()
                    .message("The following properties were not validated: {{properties}}.",
                            String.join("', '", properties))
                    .mitigation("Please check the documentation of the adapter for valid properties and the spelling.")
                    .toString());
        }
    }
}
