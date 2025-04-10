package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AndValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~short-circuiting-and-validation~1]
    @Test
    void testWhenFirstAndSecondValidationAreSuccessfulThenTheCombinationIsSuccessful() {
        final ValidationContext context = createContextWithEmptyProperties();
        final PropertyValidator mockValidator = mock(PropertyValidator.class);
        when(mockValidator.validate()).thenReturn(ValidationResult.success());
        final ValidationResult result = createValidatorFactoryWithEmptyProperties()
                .and(mockValidator, mockValidator).validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~short-circuiting-and-validation~1]
    @Test
    void testWhenFirstValidationFailsThenTheCombinationFailsAtThatPoint() {
        final ValidationContext context = createContextWithEmptyProperties();
        final PropertyValidator mockValidator1 = mock(PropertyValidator.class);
        final PropertyValidator mockValidator2 = mock(PropertyValidator.class);
        when(mockValidator1.validate()).thenReturn(new ValidationResult(false, "First validation failed"));
        when(mockValidator2.validate()).thenReturn(ValidationResult.success());
        final ValidationResult result = createValidatorFactoryWithEmptyProperties()
                .and(mockValidator1, mockValidator2).validate();
        assertThat(result.isValid(), equalTo(false));
        assertThat(result.getMessage(), equalTo("First validation failed"));
    }
}