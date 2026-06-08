package com.exasol.test.locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Locale;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
class LocaleExtensionTest {
    private static final Locale ORIGINAL_LOCALE = Locale.getDefault();

    @Test
    @Order(1)
    @WithLocale("tr")
    void testWithLocaleChangesDefaultLocaleDuringTest() {
        assertAll(() -> assertThat(Locale.getDefault(), not(equalTo(ORIGINAL_LOCALE))),
                () -> assertThat(Locale.getDefault(), equalTo(Locale.forLanguageTag("tr"))));
    }

    @Test
    @Order(2)
    void testLocaleIsRestoredAfterAnnotatedTest() {
        assertThat(Locale.getDefault(), equalTo(ORIGINAL_LOCALE));
    }
}
