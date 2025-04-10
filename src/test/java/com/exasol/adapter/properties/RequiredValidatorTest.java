package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class RequiredValidatorTest extends AbstractPropertyValidatorTest{

    // [utest -> dsn~validating-the-existence-of-mandatory-properties~1]
    @Test
    void testWhenMandatoryPropertyIsPresentThenValidationSucceeds() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("MANDATORY_PROPERTY", "present");
        final PropertyValidator validator = factory.required("MANDATORY_PROPERTY", "");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-the-existence-of-mandatory-properties~1]
    @ParameterizedTest
    @MethodSource("variationsWithMissingProperty")
    void testWhenMandatoryPropertyIsMissingThenValidationFails(Map<String, String> properties) {
        final ValidatorFactory factory = createValidatorFactoryWithEmptyProperties();
        final PropertyValidator validator = factory.required("MANDATORY_PROPERTY", "E-VSCOMJAVA-43");
        final ValidationResult result = validator.validate();
        assertAll( //
                () -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-43: The mandatory property 'MANDATORY_PROPERTY' is missing.")));
    }

    private static Stream<Arguments> variationsWithMissingProperty() {
        final Map<String, String> propertyWithNull = new HashMap<>();
        propertyWithNull.put("MANDATORY_PROPERTY", null);
        return Stream.of(Arguments.of(Collections.emptyMap()), Arguments.of(propertyWithNull));
    }

    // [utest -> dsn~validating-the-existence-of-mandatory-properties~1]
    @Test
    void testWhenMandatoryPropertyIsEmptyThenValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("EMPTY_PROPERTY", "");
        final PropertyValidator validator = factory.required("EMPTY_PROPERTY", "E-VSCOMJAVA-43");
        final ValidationResult result = validator.validate();
        assertAll( //
                () -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-43: The mandatory property 'EMPTY_PROPERTY' is empty.")));
    }

    // [utest -> dsn~validating-the-absence-of-unwanted-properties~1]
    @Test
    void testWhenAnUnwantedPropertyIsSetThenValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("UNWANTED_PROPERTY", "present");
        final PropertyValidator validator = factory.unwanted("UNWANTED_PROPERTY", "E-VSCOMJAVA-52");
        final ValidationResult result = validator.validate();
        assertAll( //
                () -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(), equalTo(
                        "E-VSCOMJAVA-52: The unwanted property 'UNWANTED_PROPERTY' is set. Please remove the property.")));
    }
}