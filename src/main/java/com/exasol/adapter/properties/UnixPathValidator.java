package com.exasol.adapter.properties;

import com.exasol.errorreporting.ExaError;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Validator for Unix Paths.
 * <p>
 * Some virtual schema adapters require files in BucketFS and paths to them in an adapter property. This validator's
 * purpose is to restrict path content to safe paths inside the change root of a BucketFS bucket.
 * </p>
 */
public class UnixPathValidator extends AbstractPropertyValidator {
    UnixPathValidator(final ValidationContext context, final String propertyName) {
        super(context, propertyName);
    }

    /**
     * Validates the value of the associated property to ensure it represents a valid Unix file path.
     *
     * <p>
     * This method performs the following validations:
     * </p>
     * <ol>
     * <li>Checks for forbidden path content such as path traversal characters ('.', '..', '//'), null characters, or
     * any other invalid sequences.</li>
     * <li>Tries to parse the property value into a {@link Path} to confirm it is a valid path.</li>
     * </ol>
     * <p>
     * If the property fails validation, a failure {@code ValidationResult} is returned that encapsulates the details of
     * the failed validation and includes a mitigation message. If the property passes all checks, a success
     * {@code ValidationResult} is returned.
     * </p>
     * 
     * @return success if the value is a valid Unix path, or failure with an appropriate error message if the validation
     *         fails.
     */
    // [impl -> dsn~validating-unix-paths~1]
    @Override
    protected ValidationResult performSpecificValidation() {
        final String value = this.getValue();
        if (hasForbiddenPathContent(value)) {
            return createFailureMessage();
        } else {
            try {
                // Path.of catches typical illegal contents like null characters.
                Path.of(value);
            } catch (final InvalidPathException exception) {
                return createFailureMessage();
            }
            return ValidationResult.success();
        }
    }

    private boolean hasForbiddenPathContent(final String value) {
        return value.isBlank() //
                || value.contains("..") //
                || value.contains("//") //
                || value.contains("/./") //
                || value.contains("%") //
                || value.contains("\t") //
                || value.contains("\n") //
                || value.contains("\r") //
                || value.contains("\\") //
                || value.startsWith("./") //
                || value.endsWith("/.") //
                || value.contains(":");
    }

    private ValidationResult createFailureMessage() {
        return ValidationResult.failure(
                ExaError.messageBuilder("E-VSCOMJAVA-59")
                        .message("The property {{property}} contains an invalid path: {{path}}.",
                                this.propertyName, this.getValue())
                        .mitigation("Please remove any kind of path traversal characters ('.', '..', '//')."));
    }
}