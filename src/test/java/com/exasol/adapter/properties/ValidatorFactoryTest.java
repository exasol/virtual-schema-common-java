package com.exasol.adapter.properties;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

    private static Stream<Arguments> generateCombinationTestData() {
        return Stream.of(Arguments.of(true, Map.of("BOOLEAN_PROPERTY", "true")),
                Arguments.of(false, Map.of("BOOLEAN_PROPERTY", "illegal value")),
                Arguments.of(false, Collections.emptyMap()),
                Arguments.of(false, Map.of("UNEXPECTED_BOOLEAN_PROPERTY", "true")));
    }

    @MethodSource("generateCombinationTestData")
    @ParameterizedTest
    void testCombinationOfValidators(final boolean expectedResult, final Map<String, String> properties) {
        final ValidationContext context = new ValidationContext(new AdapterProperties(properties),
                new ValidationLog(), Set.of("E-FOO-1", "E-FOO-2"));
        final ValidatorFactory v = ValidatorFactory.create(context);
        final PropertyValidator validator = v.and( //
                v.required("BOOLEAN_PROPERTY", "E-FOO-2"), //
                v.bool("BOOLEAN_PROPERTY", "E-FOO-1"), //
                v.complete());
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(expectedResult));
    }
}
