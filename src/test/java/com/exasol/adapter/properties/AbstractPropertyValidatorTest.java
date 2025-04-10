package com.exasol.adapter.properties;

import java.util.Collections;
import java.util.Map;

public class AbstractPropertyValidatorTest {
    protected ValidationContext createContext(final Map<String, String> properties) {
        return new ValidationContext(new AdapterProperties(properties), new ValidationLog(), properties.keySet());
    }

    protected ValidationContext createContextWithProperties(final String p1, final String v1) {
        return createContext(Map.of(p1, v1));
    }

    protected ValidationContext createContextWithProperties(final String p1, final String v1, final String p2,
                                                            final String v2) {
        return createContext(Map.of(p1, v1, p2, v2));
    }

    protected ValidationContext createContextWithProperties(final String p1, final String v1, final String p2,
                                                            final String v2, final String p3, final String v3) {
        return createContext(Map.of(p1, v1, p2, v2, p3, v3));
    }

    protected ValidationContext createContextWithProperties(final String p1, final String v1, final String p2,
                                                            final String v2, final String p3, final String v3, final String p4, final String v4) {
        return createContext(Map.of(p1, v1, p2, v2, p3, v3, p4, v4));
    }

    protected ValidationContext createContextWithEmptyProperties() {
        return createContext(Collections.emptyMap());
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final Map<String, String> properties) {
        return ValidatorFactory.create(createContext(properties));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1,
                                                                    final String p2, final String v2) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1, p2, v2));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1,
                                                                    final String p2, final String v2, final String p3, final String v3) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1, p2, v2, p3, v3));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1,
                                                                    final String p2, final String v2, final String p3, final String v3, final String p4, final String v4) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1, p2, v2, p3, v3, p4, v4));
    }

    protected ValidatorFactory createValidatorFactoryWithEmptyProperties() {
        return createValidatorFactoryWithProperties(Collections.emptyMap());
    }
}