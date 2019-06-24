package com.exasol.adapter.capabilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.exasol.adapter.capabilities.CapabilityAssertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    void substractCapabilities() {
        final MainCapability[] mainCapabilities = { MainCapability.AGGREGATE_GROUP_BY_COLUMN,
                MainCapability.AGGREGATE_GROUP_BY_EXPRESSION };
        final LiteralCapability[] literalCapabilities = { LiteralCapability.DATE, LiteralCapability.DOUBLE };
        final PredicateCapability[] predicateCapabilities = { PredicateCapability.EQUAL, PredicateCapability.BETWEEN };
        final ScalarFunctionCapability[] scalarFunctionCapabilities = { ScalarFunctionCapability.ADD,
                ScalarFunctionCapability.ABS };
        final AggregateFunctionCapability[] aggregateFunctionCapabilities = {
                AggregateFunctionCapability.APPROXIMATE_COUNT_DISTINCT, AggregateFunctionCapability.AVG };
        final Capabilities capabilities = this.builder.addMain(mainCapabilities).addLiteral(literalCapabilities)
                .addPredicate(predicateCapabilities).addScalarFunction(scalarFunctionCapabilities)
                .addAggregateFunction(aggregateFunctionCapabilities).build();

        this.builder = Capabilities.builder();
        final MainCapability[] mainCapabilitiesToExclude = { MainCapability.AGGREGATE_GROUP_BY_COLUMN };
        final LiteralCapability[] literalCapabilitiesToExclude = { LiteralCapability.DATE };
        final PredicateCapability[] predicateCapabilitiesToExclude = { PredicateCapability.EQUAL };
        final ScalarFunctionCapability[] scalarFunctionCapabilitiesToExclude = { ScalarFunctionCapability.ADD };
        final AggregateFunctionCapability[] aggregateFunctionCapabilitiesToExclude = {
                AggregateFunctionCapability.APPROXIMATE_COUNT_DISTINCT };
        final Capabilities capabilitiesToExclude = this.builder.addMain(mainCapabilitiesToExclude)
                .addLiteral(literalCapabilitiesToExclude).addPredicate(predicateCapabilitiesToExclude)
                .addScalarFunction(scalarFunctionCapabilitiesToExclude)
                .addAggregateFunction(aggregateFunctionCapabilitiesToExclude).build();

        final Capabilities capabilitiesWithExclusion = capabilities.subtractCapabilities(capabilitiesToExclude);
        assertAll(
                () -> assertThat(capabilitiesWithExclusion.getMainCapabilities(),
                        contains(MainCapability.AGGREGATE_GROUP_BY_EXPRESSION)),
                () -> assertThat(capabilitiesWithExclusion.getLiteralCapabilities(),
                        containsInAnyOrder(LiteralCapability.DOUBLE)),
                () -> assertThat(capabilitiesWithExclusion.getPredicateCapabilities(),
                        containsInAnyOrder(PredicateCapability.BETWEEN)),
                () -> assertThat(capabilitiesWithExclusion.getScalarFunctionCapabilities(),
                        containsInAnyOrder(ScalarFunctionCapability.ABS)),
                () -> assertThat(capabilitiesWithExclusion.getAggregateFunctionCapabilities(),
                        containsInAnyOrder(AggregateFunctionCapability.AVG)));
    }
}