package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

class ValidationContextTest {
    @Test
    void testConstructingContext() {
        final AdapterProperties properties = AdapterProperties.emptyProperties();
        final ValidationContext context = new ValidationContext(properties);
        assertThat(context.getProperties(), sameInstance(properties));
    }
}