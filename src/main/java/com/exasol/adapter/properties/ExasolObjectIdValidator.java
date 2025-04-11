package com.exasol.adapter.properties;

import com.exasol.db.ExasolIdentifier;
import com.exasol.errorreporting.ExaError;

/**
 * Validates that a property value adheres to the format of an Exasol database object identifier.
 *
 * <p>
 * Checks the property value against the rules for valid Exasol identifiers and returns a validation result.
 * </p>
 */
class ExasolObjectIdValidator extends AbstractPropertyValidator {
    private static final String ID_DOC_URL = "https://docs.exasol.com/db/latest/sql_references/basiclanguageelements.htm#SQLidentifier";

    /**
     * Create a new instance of {@link ExasolObjectIdValidator}.
     *
     * <p>
     * Validates if the property value adheres to the format requirements for Exasol database object identifiers.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property to validate
     */
    ExasolObjectIdValidator(final ValidationContext context, final String propertyName) {
        super(context, propertyName);
    }

    /**
     * Validate if the property value conforms to the format of an Exasol database object identifier.
     *
     * @return result of the validation indicating success or failure with an appropriate error message
     */
    // [impl -> dsn~validating-exasol-object-id-properties~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        if (ExasolIdentifier.validate(this.getValue())) {
            return ValidationResult.success();
        } else {
            return new ValidationResult(false, ExaError.messageBuilder("E-VSCOMJAVA-45") //
                    .message("Property {{property}} is not a valid Exasol database object identifier.",
                            this.propertyName) //
                    .mitigation("Please refer to " + ID_DOC_URL + " for the correct format guidelines.") //
                    .toString());
        }
    }
}
