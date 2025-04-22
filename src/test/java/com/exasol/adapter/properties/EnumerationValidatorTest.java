package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class EnumerationValidatorTest extends AbstractPropertyValidatorTest {
    private enum the_enum {
        A, B, C
    }

    // [utest -> dsn~validating-enumeration-properties~1]
    @Test
    void testWhenValueIsInEnumerationThenValidationSucceeds() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_ENUM", "A");
        final PropertyValidator validator = factory.enumeration("THE_ENUM", the_enum.class);
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-enumeration-properties~1]
    @Test
    void testWhenValueIsNotInEnumerationThenValidationFails() {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_ENUM", "D");
        final PropertyValidator validator = factory.enumeration("THE_ENUM", the_enum.class);
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-44: The property 'THE_ENUM' has an invalid value 'D'."
                                + " Please pick one of the following values: 'A', 'B', 'C'")));
    }

    // [utest -> dsn~validating-enumeration-properties~1]
    @Test
    void testWhenValueIsNullThenValidationFails() {
        final Map<String, String> propertiesWithNull = new HashMap<>();
        propertiesWithNull.put("THE_ENUM", null);
        final AdapterProperties properties = new AdapterProperties(propertiesWithNull);
        final ValidatorFactory factory = ValidatorFactory.create(properties);
        final PropertyValidator validator = factory.enumeration("THE_ENUM", the_enum.class);
        final ValidationResult result = validator.validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-44: The property 'THE_ENUM' has an invalid value <null>."
                                + " Please pick one of the following values: 'A', 'B', 'C'")));
    }


}
