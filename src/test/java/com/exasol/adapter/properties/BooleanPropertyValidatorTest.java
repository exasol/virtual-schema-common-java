package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class BooleanPropertyValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validating-boolean-properties~1]
    @ValueSource(strings = { "TRUE", "FALSE", "true", "false" })
    @ParameterizedTest
    void testWhenPropertyContainsValidValueValidationSucceeds(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties(
                AdapterProperties.IGNORE_ERRORS_PROPERTY, value);
        final ValidationResult result = factory.bool(AdapterProperties.IGNORE_ERRORS_PROPERTY, "").validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-boolean-properties~1]
    @Test
    void testWhenPropertyContainsInvalidValueValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties(
                AdapterProperties.IGNORE_ERRORS_PROPERTY, "SOME STRING");
        final ValidationResult result = factory.bool(AdapterProperties.IGNORE_ERRORS_PROPERTY, "E-VSCOMJAVA-42")
                .validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo("E-VSCOMJAVA-42: The value 'SOME STRING' for property '"
                        + AdapterProperties.IGNORE_ERRORS_PROPERTY + "' must be either 'true' or 'false'.")));
    }
}