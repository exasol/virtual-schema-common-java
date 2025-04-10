package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertAll;

class ValidationContextTest {
    @Test
    void testConstructingContext() {
        final AdapterProperties properties = AdapterProperties.emptyProperties();
        final ValidationLog validationLog = new ValidationLog();
        final ValidationContext context = new ValidationContext(properties, validationLog, Collections.emptySet());
        assertAll(() -> assertThat(context.getProperties(), sameInstance(properties)),
                () -> assertThat(context.getValidationLog(), sameInstance(validationLog)));
    }
}
