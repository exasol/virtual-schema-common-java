package com.exasol.adapter.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;

class MultiSelectValidatorTest extends AbstractPropertyValidatorTest {
    private enum MULTI_SELECT_ENUM {
        ONE, TWO, THREE, FOUR
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @ValueSource(strings = { //
            "ONE", //
            "TWO", //
            "ONE,TWO", //
            "ONE ,TWO", //
            "ONE, TWO", //
            "ONE , TWO", //
            "ONE\t,\tTWO", //
            "ONE, TWO, THREE" //
    })
    @ParameterizedTest
    void testWhenKnownEnumValueIsGivenThenValidationSucceeds(final String propertyValue) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_MS_PROPERTY", propertyValue);
        final PropertyValidator validator = factory.multiSelect("THE_MS_PROPERTY", MULTI_SELECT_ENUM.class);
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    private static Stream<Arguments> unknownEnumValuesProvider() {
        return Stream.of(
                Arguments.of("FIVE", "'FIVE'"),
                Arguments.of("one", "'one'"),
                Arguments.of("ONETWO", "'ONETWO'"),
                Arguments.of("ONE,TWO,SIX,SEVEN", "'SIX', 'SEVEN'")
        );
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @MethodSource("unknownEnumValuesProvider")
    @ParameterizedTest
    void testWhenUnknownEnumValueIsGivenThenValidationFails(final String propertyValue, final String reportedWrong) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_MS_PROPERTY", propertyValue);
        final PropertyValidator validator = factory.multiSelect("THE_MS_PROPERTY", MULTI_SELECT_ENUM.class);
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo(
                        "E-VSCOMJAVA-57: The following values given for the property 'THE_MS_PROPERTY' are unknown: " + reportedWrong
                                + " Please use one or more of the following values: 'ONE', 'TWO', 'THREE', 'FOUR'."
                                + " Separate the individual values with a comma.")));
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @ValueSource(strings = { //
            "", //
            " ", //
            "  ", //
            "\t", //
            "\n\t", //
            "\n", //
            " , ", //
            ",," //
    })
    @ParameterizedTest
    void testWhenValueIsEmptyAndEmptyValueIsAllowedThenValidationSucceeds(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("EMPTY_ALLOWED", value);
        final PropertyValidator validator = factory.multiSelect("EMPTY_ALLOWED", MULTI_SELECT_ENUM.class, true);
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @ValueSource(strings = { //
            "", //
            " ", //
            "  ", //
            "\t", //
            "\n\t", //
            "\n", //
            " , ", //
            ",," //
    })
    @ParameterizedTest
    void testWhenValueIsEmptyAndEmptyValueIsNotAllowedThenValidationFails(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("EMPTY_NOT_ALLOWED", value);
        final PropertyValidator validator = factory.multiSelect("EMPTY_NOT_ALLOWED", MULTI_SELECT_ENUM.class);
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo(
                        "E-VSCOMJAVA-56: The property 'EMPTY_NOT_ALLOWED' must have at least one value set."
                                + " Please select at least one of the following values: 'ONE', 'TWO', 'THREE', 'FOUR'."
                                + " Separate the individual values with a comma.")));
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @Test
    void testWhenValueIsNullAndEmptyValueIsAllowedThenValidationSucceeds() {
        final Map<String, String> mapWithNull = new HashMap<>(1);
        mapWithNull.put("NULL_ALLOWED", null);
        final ValidatorFactory factory = ValidatorFactory.create(new AdapterProperties(mapWithNull));
        final PropertyValidator validator = factory.multiSelect("NULL_ALLOWED", MULTI_SELECT_ENUM.class, true);
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-multi-select-properties~1]
    @Test
    void testWhenValueIsNullAndEmptyValueIsNotAllowedThenValidationFails() {
        final Map<String, String> mapWithNull = new HashMap<>(1);
        mapWithNull.put("NULL_FORBIDDEN", null);
        final ValidatorFactory factory = ValidatorFactory.create(new AdapterProperties(mapWithNull));
        final PropertyValidator validator = factory.multiSelect("NULL_FORBIDDEN", MULTI_SELECT_ENUM.class);
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo(
                        "E-VSCOMJAVA-56: The property 'NULL_FORBIDDEN' must have at least one value set."
                                + " Please select at least one of the following values: 'ONE', 'TWO', 'THREE', 'FOUR'."
                                + " Separate the individual values with a comma.")));
    }
}