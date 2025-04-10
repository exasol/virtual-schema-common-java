package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class StringValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validating-a-string-against-a-regular-expression~1]
    @CsvSource({ //
            "1, \\d", //
            "1234567890, \\d{10}", //
            "A:1, \\w:\\d" //
    })
    @ParameterizedTest
    void testWhenThePropertyValueMatchesTheRegExThenValidationSucceeds(final String value, final String regEx) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_STRING", value);
        final StringValidator validator = factory.matches("THE_STRING", "E-VSCOMJAVA-51", Pattern.compile(regEx));
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-a-string-against-a-regular-expression~1]
    @CsvSource({ //
            "1, \\d{2}", //
            "12345, \\d{10}", //
            "A-1, \\w:\\d" //
    })
    @ParameterizedTest
    void testWhenThePropertyValueDoesNotMatchTheRegExThenValidationFails(final String value, final String regEx) {
        final Pattern pattern = Pattern.compile(regEx);
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_STRING", value);
        final StringValidator validator = factory.matches("THE_STRING", "E-VSCOMJAVA-51", pattern);
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-51: The property 'THE_STRING' has an invalid value '" + value
                                + "'. Please use a value matching the regular expression '" + pattern + "'.")),
                () -> assertThat(result.isValid(), equalTo(false)));
    }

    // [utest -> dsn~reporting-format-violations-in-properties~1]
    @Test
    void testWhenAnExplanationForFormatIsGivenThenThisIsUsedInTheErrorMessageInsteadOfPattern() {
        final Pattern pattern = Pattern.compile("^[^:]+:\\d+$");
        final ValidatorFactory factory = createValidatorFactoryWithProperties("ADDRESS", "server.example.com:8080X");
        final StringValidator validator = factory.matches("ADDRESS", "E-VSCOMJAVA-51", pattern, "<host>:<port>");
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-51: The property 'ADDRESS' has an invalid value 'server.example.com:8080X'"
                                + ". Please use the format '<host>:<port>'.")),
                () -> assertThat(result.isValid(), equalTo(false)));
    }
}