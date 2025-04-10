package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class AllOfValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~all-of-validation~1]
    @Test
    void testWhenAllValidationsAreSuccessfulThenTheCombinationIsSuccessful() {
        final ValidatorFactory factory = createValidatorFactoryWithEmptyProperties();
        final PropertyValidator mockValidator = mock(PropertyValidator.class);
        when(mockValidator.validate()).thenReturn(ValidationResult.success());
        final ValidationResult result = factory.allOf(mockValidator, mockValidator).validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~all-of-validation~1]
    @Test
    void testWhenFirstValidationFailsTheCombinationFailsButProcessesAllValidators() {
        final ValidatorFactory factory = createValidatorFactoryWithEmptyProperties();
        final PropertyValidator mockValidator1 = mock(PropertyValidator.class);
        final PropertyValidator mockValidator2 = mock(PropertyValidator.class);
        when(mockValidator1.validate()).thenReturn(new ValidationResult(false, "First validation failed"));
        when(mockValidator2.validate()).thenReturn(ValidationResult.success());
        final ValidationResult result = factory.allOf(mockValidator1, mockValidator2).validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(), equalTo("First validation failed")),
                () -> verify(mockValidator2, times(1)).validate());
    }

    // [utest -> dsn~all-of-validation~1]
    @Test
    void testWhenMultipleValidationsFailTheCombinationCollectsAllErrors() {
        final ValidatorFactory factory = createValidatorFactoryWithEmptyProperties();
        final PropertyValidator mockValidator1 = mock(PropertyValidator.class);
        final PropertyValidator mockValidator2 = mock(PropertyValidator.class);
        final PropertyValidator mockValidator3 = mock(PropertyValidator.class);
        when(mockValidator1.validate()).thenReturn(new ValidationResult(false, "First validation failed"));
        when(mockValidator2.validate()).thenReturn(new ValidationResult(false, "Second validation failed"));
        when(mockValidator3.validate()).thenReturn(ValidationResult.success());
        final ValidationResult result = factory.allOf(mockValidator1, mockValidator2, mockValidator3)
                .validate();
        assertAll(() -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage().contains("First validation failed"), equalTo(true)),
                () -> assertThat(result.getMessage().contains("Second validation failed"), equalTo(true)),
                () -> verify(mockValidator3, times(1)).validate());
    }
}