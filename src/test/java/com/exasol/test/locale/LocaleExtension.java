package com.exasol.test.locale;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LocaleExtension implements BeforeEachCallback, AfterEachCallback {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(LocaleExtension.class);
    private static final String DEFAULT_LOCALE_KEY = "defaultLocale";

    @Override
    public void beforeEach(final ExtensionContext context) {
        findLocaleTag(context).ifPresent(localeTag -> {
            context.getStore(NAMESPACE).put(DEFAULT_LOCALE_KEY, Locale.getDefault());
            Locale.setDefault(Locale.forLanguageTag(localeTag));
        });
    }

    @Override
    public void afterEach(final ExtensionContext context) {
        final Locale defaultLocale = context.getStore(NAMESPACE).remove(DEFAULT_LOCALE_KEY, Locale.class);
        if (defaultLocale != null) {
            Locale.setDefault(defaultLocale);
        }
    }

    private Optional<String> findLocaleTag(final ExtensionContext context) {
        final Optional<String> methodLocaleTag = context.getTestMethod()
                .map(method -> method.getAnnotation(WithLocale.class))
                .filter(annotation -> annotation != null)
                .map(WithLocale::value);
        if (methodLocaleTag.isPresent()) {
            return methodLocaleTag;
        }
        return context.getTestClass()
                .map(testClass -> testClass.getAnnotation(WithLocale.class))
                .filter(annotation -> annotation != null)
                .map(WithLocale::value);
    }
}
