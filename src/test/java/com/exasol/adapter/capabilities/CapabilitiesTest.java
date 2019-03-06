package com.exasol.adapter.capabilities;

import static com.exasol.adapter.capabilities.CapabilityAssertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CapabilitiesTest {
    private Capabilities.Builder builder;

    @BeforeEach
    void beforeEach() {
        this.builder = Capabilities.builder();
    }

    @Test
    void testCreateEmptyCapabilities() {
        final Capabilities capabilities = this.builder.build();
        assertAll(() -> assertEmptyMainCapabilities(capabilities), //
                () -> assertEmptyLiteralCapabilities(capabilities), //
                () -> assertEmptyPredicateCapabilities(capabilities), //
                () -> assertEmptyScalarFunctionCapabilities(capabilities), //
                () -> assertEmptyAggregateFunctionCapatilities(capabilities));
    }

    @Test
    void buildWithMainCapabilities() {
        final MainCapability[] expectedCapabilities = { MainCapability.AGGREGATE_GROUP_BY_COLUMN,
                MainCapability.AGGREGATE_GROUP_BY_EXPRESSION };
        final Capabilities capabilities = this.builder.addMain(expectedCapabilities).build();
        assertAll(() -> assertCapabilitesContainAllOf(capabilities, expectedCapabilities), //
                () -> assertEmptyLiteralCapabilities(capabilities), //
                () -> assertEmptyPredicateCapabilities(capabilities), //
                () -> assertEmptyScalarFunctionCapabilities(capabilities), //
                () -> assertEmptyAggregateFunctionCapatilities(capabilities));
    }

    @Test
    void buildWithLiteralCapabilities() {
        final LiteralCapability[] expectedCapabilities = { LiteralCapability.BOOL, LiteralCapability.DATE };
        final Capabilities capabilities = this.builder.addLiteral(expectedCapabilities).build();
        assertAll(() -> assertEmptyMainCapabilities(capabilities), //
                () -> assertCapabilitesContainAllOf(capabilities, expectedCapabilities), //
                () -> assertEmptyPredicateCapabilities(capabilities), //
                () -> assertEmptyScalarFunctionCapabilities(capabilities), //
                () -> assertEmptyAggregateFunctionCapatilities(capabilities));
    }

    @Test
    void buildWithPredicateCapabilities() {
        final PredicateCapability[] expectedCapabilities = { PredicateCapability.AND, PredicateCapability.BETWEEN };
        final Capabilities capabilities = this.builder.addPredicate(expectedCapabilities).build();
        assertAll(() -> assertEmptyMainCapabilities(capabilities), //
                () -> assertEmptyLiteralCapabilities(capabilities), //
                () -> assertCapabilitesContainAllOf(capabilities, expectedCapabilities), //
                () -> assertEmptyScalarFunctionCapabilities(capabilities), //
                () -> assertEmptyAggregateFunctionCapatilities(capabilities));
    }

    @Test
    void buildWithScalarFunctionCapabilities() {
        final ScalarFunctionCapability[] expectedCapabilities = { ScalarFunctionCapability.ABS,
                ScalarFunctionCapability.ACOS };
        final Capabilities capabilities = this.builder.addScalarFunction(expectedCapabilities).build();
        assertAll(() -> assertEmptyMainCapabilities(capabilities), //
                () -> assertEmptyLiteralCapabilities(capabilities), //
                () -> assertEmptyPredicateCapabilities(capabilities), //
                () -> assertCapabilitesContainAllOf(capabilities, expectedCapabilities), //
                () -> assertEmptyAggregateFunctionCapatilities(capabilities));
    }

    @Test
    void buildWithAggregateFunctionCapabilities() {
        final AggregateFunctionCapability[] expectedCapabilities = {
                AggregateFunctionCapability.APPROXIMATE_COUNT_DISTINCT, AggregateFunctionCapability.AVG };
        final Capabilities capabilities = this.builder.addAggregateFunction(expectedCapabilities).build();
        assertAll(() -> assertEmptyMainCapabilities(capabilities), //
                () -> assertEmptyLiteralCapabilities(capabilities), //
                () -> assertEmptyPredicateCapabilities(capabilities), //
                () -> assertEmptyScalarFunctionCapabilities(capabilities), //
                () -> assertCapabilitesContainAllOf(capabilities, expectedCapabilities));
    }
}