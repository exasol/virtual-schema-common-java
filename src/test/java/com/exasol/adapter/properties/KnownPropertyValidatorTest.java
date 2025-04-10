package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class KnownPropertyValidatorTest extends AbstractPropertyValidatorTest {
    @Test
    void testWhenOnlyKnownPropertiesWereUsedThenValidationSucceeds() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", "V1", "P2", "V2");
        final KnownPropertyValidator validator = factory.known("P1", "P2");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    @Test
    void testWhenUnknownPropertyWasUsedThenValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", "V1", "P2", "V2", "P3", "V3");
        final KnownPropertyValidator validator = factory.known("P1", "P2");
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo("E-VSCOMJAVA-44: Unknown adapter properties specified: 'P2', 'P3'."
                        + " Please check the documentation of the adapter for valid properties and the spelling."))
        );
    }
}
