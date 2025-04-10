package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class CompleteValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validation-completeness-check~1]
    @Test
    void testWhenAllPropertiesWereValidatedThenIndicateSuccess() {
        final ValidationContext context = createContextWithProperties("P1", "V1", "P2", "V2");
        final CompleteValidator validator = new CompleteValidator(context);
        new DummyValidator(context, "P1").validate();
        new DummyValidator(context, "P2").validate();
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validation-completeness-check~1]
    @Test
    void testWhenNotAllPropertiesWereValidatedThenIndicateFailure() {
        final ValidationContext context = createContextWithProperties("P1", "V1", "P2", "V2", "P3", "V3");
        final CompleteValidator validator = new CompleteValidator(context);
        new DummyValidator(context, "P1").validate();
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), () -> assertThat(result.getMessage(),
                equalTo("E-VSCOMJAVA-46: The following properties were not validated: 'P2', 'P3'"
                        + ". Please check the documentation of the adapter for valid properties and the spelling.")));
    }

    private static final class DummyValidator extends AbstractPropertyValidator {
        DummyValidator(final ValidationContext context, final String propertyName) {
            super(context, propertyName, "E-DUMMY-1");
        }

        @Override
        protected ValidationResult performSpecificValidation() {
            return ValidationResult.success();
        }
    }

    @Test
    void testWhenAnUnknownPropertyIsProvidedThenIndicateFailure() {
        final AdapterProperties adapterProperties = new AdapterProperties(
                Map.of("P1", "V1", "UNKNOWN", "SOME VALUE", "MORE_UNKNOWN", "ANOTHER VALUE"));
        final ValidationLog log = new ValidationLog();
        final Set<String> knownPropertyNames = Set.of("P1");
        final ValidationContext context = new ValidationContext(adapterProperties, log, knownPropertyNames);
        final CompleteValidator validator = new CompleteValidator(context);
        new DummyValidator(context, "P1").validate();
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), () -> assertThat(result.getMessage(),
                equalTo("E-VSCOMJAVA-45: The following properties are unknown: 'UNKNOWN', 'MORE_UNKNOWN'."
                        + " Please check the documentation of the adapter for valid properties and the spelling.")));
    }
}
