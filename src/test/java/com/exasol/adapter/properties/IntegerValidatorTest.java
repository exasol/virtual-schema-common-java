package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerValidatorTest extends AbstractPropertyValidatorTest {
    @ValueSource(strings = { "" + Long.MIN_VALUE, "-1", "0", "1", "" + Long.MAX_VALUE })
    @ParameterizedTest
    // [utest -> dsn~validating-integer-properties~1]
    void testWhenValidIntegerIsGivenWithOutBoundariesThenValidationSucceeds(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_INTEGER", value);
        final PropertyValidator validator = factory.integer("THE_INTEGER");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    @ValueSource(strings = { "SOME STRING", "-1.0", "0.0", "1.0" })
    @ParameterizedTest
    // [utest -> dsn~validating-integer-properties~1]
    void testWhenInvalidIntegerIsGivenThenValidationFails(final String value) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_INTEGER", value);
        final PropertyValidator validator = factory.integer("THE_INTEGER");
        final ValidationResult result = validator.validate();
        assertAll( //
                () -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(), equalTo("E-VSCOMJAVA-47: The value '" + value
                        + "' for property 'THE_INTEGER' is not a valid integer number.")));
    }

    @CsvSource({ //
            "0, 1, 2", //
            "1, 1, 1", //
            Long.MIN_VALUE + ", 1 ," + Long.MAX_VALUE, //
            "-1, 0, 1" })
    @ParameterizedTest
    // [utest -> dsn~integer-interval-validation~1]
    void testWhenBoundariesAreProvidedAndValueIsBetweenBoundariesThenValidationSucceeds(final long min,
            final String value, final long max) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_INTEGER", value);
        final PropertyValidator validator = factory.integer("THE_INTEGER", min, max);
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    @CsvSource({ //
            "0, -1, 2", //
            "1, 0, 1", //
            (Long.MIN_VALUE + 1) + ", " + Long.MIN_VALUE + " ," + Long.MAX_VALUE, //
            "-1, 2, 1" })
    @ParameterizedTest
    // [utest -> dsn~integer-interval-validation~1]
    void testWhenBoundariesAreProvidedAndValueIsOutsideBoundariesThenValidationFails(final long min, final String value,
            final long max) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_INTEGER", value);
        final PropertyValidator validator = factory.integer("THE_INTEGER", min, max);
        final ValidationResult result = validator.validate();
        assertAll( //
                () -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-46: The value for property 'THE_INTEGER' must be between " + min + " and "
                                + max + ", but was " + value + ".")));
    }

    @Test
    // [utest -> dsn~integer-interval-validation~1]
    void testWhenSettingUpperBoundaryBelowLowerBoundaryThenExceptionIsThrown() {
        final ValidatorFactory factory = createValidatorFactoryWithEmptyProperties();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.integer("THE_INTEGER", 1, -1));
        assertThat(exception.getMessage(), startsWith(
                "E-VSCOMJAVA-54: The upper bound (-1) must be greater than or equal the lower bound (1) for property 'THE_INTEGER'."));

    }
}