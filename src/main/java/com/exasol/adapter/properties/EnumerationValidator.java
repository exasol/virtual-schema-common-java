package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates if a property has a value that matches one of the values in a specified enumeration.
 *
 * <p>
 * The validator checks if the current property value is a valid enum constant within the given enumeration type.
 * If the value is invalid, it produces an appropriate error message.
 * </p>
 *
 * @param <T> Type of the enumeration to validate against
 */
public class EnumerationValidator<T extends  Enum<T>> extends AbstractPropertyValidator {
    protected final Class<T> enumClass;
    protected final List<String> enumValueCache;

    /**
     * Create a new instance of {@link EnumerationValidator}.
     *
     * @param context      validation context containing properties and validation logs
     * @param propertyName name of the property being validated
     * @param enumClass    enumeration class containing the valid values for the property
     */
    EnumerationValidator(final ValidationContext context, final String propertyName,
                         final Class<T> enumClass) {
        super(context, propertyName);
        this.enumClass = enumClass;
        this.enumValueCache = getEnumValues();
    }

    private List<String> getEnumValues() {
        final List<String> enumNames = new ArrayList<>(this.enumClass.getEnumConstants().length);
        for (final Enum<T> constant : this.enumClass.getEnumConstants()) {
            enumNames.add(constant.name());
        }
        return enumNames;
    }

    /**
     * Validate the property value to ensure it matches one of the predefined enumeration values.
     *
     * @return result of the validation, either successful or containing an error message
     */
    // [impl -> dsn~validating-enumeration-properties~1]]
    @Override
    protected ValidationResult performSpecificValidation() {
        final String value = this.getValue();
        if (value == null || !this.enumValueCache.contains(value)) {
            return new ValidationResult(false, ExaError.messageBuilder("E-VSCOMJAVA-44")
                    .message("The property {{property}} has an invalid value {{value}}.", this.propertyName, value)
                    .mitigation("Please pick one of the following values: {{values}}", String.join("', '", this.enumValueCache))
                    .toString());
        } else {
            return ValidationResult.success();
        }
    }
}