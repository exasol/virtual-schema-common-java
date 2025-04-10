package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class BooleanPropertyValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validating-boolean-properties~1]
    @ValueSource(strings = { "TRUE", "FALSE", "true", "false" })
    @ParameterizedTest
    void testWhenPropertyContainsValidValueThenValidationSucceeds(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", value);
        final ValidationResult result = factory.bool("P1", "").validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-boolean-properties~1]
    @Test
    void testWhenPropertyContainsInvalidValueThenValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", "SOME STRING");
        final ValidationResult result = factory.bool("P1", "E-VSCOMJAVA-42").validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), () -> assertThat(result.getMessage(), equalTo(
                "E-VSCOMJAVA-42: The value 'SOME STRING' for property 'P1' must be either 'true' or 'false'.")));
    }

    @Test
    void testWhenPropertyIsNullThenValidationFails() {
        final Map<String, String> rawProperties = new HashMap<>();
        rawProperties.put("P1", null);
        final ValidatorFactory factory = ValidatorFactory.create(new AdapterProperties(rawProperties));
        final ValidationResult result = factory.bool("P1", "E-VSCOMJAVA-42").validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), () -> assertThat(result.getMessage(),
                equalTo("E-VSCOMJAVA-42: The value <null> for property 'P1' must be either 'true' or 'false'.")));
    }
}