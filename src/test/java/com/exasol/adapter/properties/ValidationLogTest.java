package com.exasol.adapter.properties;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

class ValidationLogTest {
    @Test
    void testWhenPropertyWasValidatedThenLogReportsItAsValidated() {
        final ValidationLog log = new ValidationLog();
        log.addValidation("THE_PROPERTY");
        assertThat(log.isValidated("THE_PROPERTY"), equalTo(true));
    }

    @Test
    void testWhenPropertyWasNotValidatedThenLogReportsItAsNotValidated() {
        final ValidationLog log = new ValidationLog();
        log.addValidation("THE_PROPERTY");
        assertThat(log.isValidated("THE_OTHER_PROPERTY"), equalTo(false));
    }

    @Test
    void testGettingValidatedProperties() {
        final ValidationLog log = new ValidationLog();
        log.addValidation("P1");
        log.addValidation("P2");
        assertThat(log.getValidatedProperties(), contains("P1", "P2"));
    }
}
