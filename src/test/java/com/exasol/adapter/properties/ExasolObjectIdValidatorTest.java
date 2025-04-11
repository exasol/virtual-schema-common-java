package com.exasol.adapter.properties;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExasolObjectIdValidatorTest extends AbstractPropertyValidatorTest {
    @ValueSource(strings = { //
            "A_valid_object_id", //
            "anotherValidID1", //
            "ÜnicodeMiddleDot·Test", //
            "Valid_ID_with_Mn̼͡", //
    })
    @ParameterizedTest
    // [utest -> dsn~validating-exasol-object-id-properties~1]
    void testWhenValidExasolObjectIdIsGivenThenValidationSucceeds(final String id) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("EXA_OBJECT_ID", id);
        final PropertyValidator validator = factory.exasolObjectId("EXA_OBJECT_ID");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    @ValueSource(strings = { //
            "", // Empty string
            "ID_with_unsupported&characters", //
            "ID with spaces", //
            "1ID_starting_with_a_number", //
            "ID-ending-with-special-char!" //
    })
    @ParameterizedTest
    // [utest -> dsn~validating-exasol-object-id-properties~1]
    void testWhenInvalidExasolObjectIdIsGivenThenValidationFails(final String id) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("EXA_OBJECT_ID", id);
        final PropertyValidator validator = factory.exasolObjectId("EXA_OBJECT_ID");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(false));
    }
}
