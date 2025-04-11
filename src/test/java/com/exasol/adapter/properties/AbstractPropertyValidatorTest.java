package com.exasol.adapter.properties;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractPropertyValidatorTest {
    protected ValidationContext createContext(final Map<String, String> properties) {
        return new ValidationContext(new AdapterProperties(properties));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final Map<String, String> properties) {
        return ValidatorFactory.create(new AdapterProperties(properties));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1, final String p2, final String v2) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1, p2, v2));
    }

    protected ValidatorFactory createValidatorFactoryWithProperties(final String p1, final String v1, final String p2, final String v2, final String p3, final String v3) {
        return createValidatorFactoryWithProperties(Map.of(p1, v1, p2, v2, p3, v3));
    }

    protected ValidatorFactory createValidatorFactoryWithEmptyProperties() {
        return createValidatorFactoryWithProperties(Collections.emptyMap());
    }
}