package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Unit tests for {@link ValidatorFactory}.
 *
 * <p>
 * Verifies combinations of property validators in different scenarios by testing their interaction and validation
 * results.
 * </p>
 * <p>
 * Please note that the tests for the individual factory methods are already covered in the unit tests for the
 * individual validators.
 * </p>
 */
// [utest -> dsn~validator-composition~1]
class ValidatorFactoryTest {
    private static Arguments testCase(final boolean expectedResult, final String... tuples) {
        Map<String, String> properties = Stream.of(tuples).map(tuple -> tuple.split("="))
                .collect(Collectors.toMap(split -> split[0], split -> split[1]));
        return Arguments.of(expectedResult, properties);
    }

    private static Stream<Arguments> generateCombinationTestData() {
        return Stream.of( //
                testCase(true, "BOOLEAN_PROPERTY=true"), testCase(false, "BOOLEAN_PROPERTY=illegal value"),
                testCase(false), testCase(false, "UNEXPECTED_BOOLEAN_PROPERTY=true"));
    }

    @MethodSource("generateCombinationTestData")
    @ParameterizedTest
    void testCombinationOfValidators(final boolean expectedResult, final Map<String, String> properties) {
        final ValidatorFactory v = ValidatorFactory.create(new AdapterProperties(properties));
        final PropertyValidator validator = //
                v.and( //
                        v.required("BOOLEAN_PROPERTY", "E-FOO-2"), //
                        v.bool("BOOLEAN_PROPERTY", "E-FOO-1"), //
                        v.allCovered() //
                );
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(expectedResult));
    }

    public static Stream<Arguments> generateTreeTestData() {
        return Stream.of( //
                testCase(true, "REQ_BASE_BOOL=true", "REQ_BASE_STRING=a1", "REQ_DIALECT_INT=1",
                        "OPT_DIALECT_BOOL=true"),
                testCase(true, "REQ_BASE_BOOL=true", "REQ_BASE_STRING=a1", "REQ_DIALECT_INT=1",
                        "OPT_DIALECT_BOOL=true"),
                testCase(false, "REQ_BASE_BOOL=true", "REQ_BASE_STRING=a1", "REQ_DIALECT_INT=1",
                        "OPT_DIALECT_BOOL=true", "UNEXPECTED_PROPERTY=true"),
                testCase(false), //
                testCase(false, "REQ_BASE_BOOL=true", "REQ_BASE_STRING=a1", "REQ_DIALECT_INT=invalid_value") //
        );
    }

    @MethodSource("generateTreeTestData")
    @ParameterizedTest
    void testConstructingValidatorTree(final boolean expectedResult, final Map<String, String> properties) {
        final ValidatorFactory v = ValidatorFactory.create(new AdapterProperties(properties));
        final PropertyValidator validator = //
                v.allOf( //
                        v.allOf( //
                                v.and( //
                                        v.required("REQ_BASE_BOOL", ""), //
                                        v.bool("REQ_BASE_BOOL", "") //
                                ), //
                                v.and( //
                                        v.required("REQ_BASE_STRING", ""), //
                                        v.matches("REQ_BASE_STRING", "", Pattern.compile("\\w\\d")) //
                                ) //
                        ), //
                        v.and( //
                                v.required("REQ_DIALECT_INT", ""), //
                                v.integer("REQ_DIALECT_INT", "") //
                        ), //
                        v.bool("OPT_DIALECT_BOOL", ""), //
                        v.allCovered()); //
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(expectedResult));
    }

    @Test
    void testRequiredShortHandForm() {
        final ValidatorFactory v = ValidatorFactory.create(AdapterProperties.emptyProperties());
        final PropertyValidator validator = v.required( //
                v.bool("P1", "E-FOO-42") //
        );
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(), equalTo("E-FOO-42: The mandatory property 'P1' is missing.")));
    }

    @Test
    void testWhenRequiredIsCombinedWithUnwantedAndAllCoveredThenUnwantedIsHandledCorrectly() {
        final ValidatorFactory v = ValidatorFactory.create(new AdapterProperties(Map.of("P1", "A", "P2", "B")));
        final PropertyValidator validator = v.allOf( //
                v.required("P1", "E-FOO-43"), //
                v.unwanted("P2", "E-FOO-44"), //
                v.allCovered());
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)), //
                () -> assertThat(result.getMessage(),
                        equalTo("E-FOO-44: The unwanted property 'P2' is set. Please remove the property."))

        );
    }
}