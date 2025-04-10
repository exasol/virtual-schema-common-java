package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class CoverageValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validation-completeness-check~1]
    @Test
    void testWhenAllPropertiesWereValidatedThenIndicateSuccess() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", "true", "P2", "false");
        factory.bool("P1", "E-IGNORE-1");
        factory.bool("P2", "E-IGNORE-2");
        final PropertyValidator validator = factory.allCovered();
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validation-completeness-check~1]
    @Test
    void testWhenNotAllPropertiesAreKnownThenIndicateFailure() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("P1", "true", "P2", "V2", "P3", "V3");
        factory.bool("P1", "E-FOO-1").validate(); // Call to mark P1 as known
        final PropertyValidator validator = factory.allCovered();
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), () -> assertThat(result.getMessage(),
                equalTo("E-VSCOMJAVA-53: The following properties are unknown: 'P2', 'P3'"
                        + ". Please check the documentation of the adapter for valid properties and the spelling.")));
    }
}