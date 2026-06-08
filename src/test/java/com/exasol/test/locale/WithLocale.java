package com.exasol.test.locale;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * This annotation can be used to set the default locale for a test method or an entire test class.
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@ExtendWith(LocaleExtension.class)
public @interface WithLocale {
    /**
     * The locale to set for the annotated test method or all test methods in the annotated class.
     */
    String value();
}
