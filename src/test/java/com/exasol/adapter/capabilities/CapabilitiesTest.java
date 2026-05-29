package com.exasol.adapter.capabilities;

import static com.exasol.adapter.capabilities.CapabilityAssertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

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
    void subtract() {
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

        final Capabilities capabilitiesWithExclusion = capabilities.subtract(capabilitiesToExclude);
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
                        containsInAnyOrder(AggregateFunctionCapability.AVG)),
                () -> assertThat(capabilities.getMainCapabilities(), containsInAnyOrder(mainCapabilities)),
                () -> assertThat(capabilities.getLiteralCapabilities(), containsInAnyOrder(literalCapabilities)),
                () -> assertThat(capabilities.getPredicateCapabilities(), containsInAnyOrder(predicateCapabilities)),
                () -> assertThat(capabilities.getScalarFunctionCapabilities(),
                        containsInAnyOrder(scalarFunctionCapabilities)),
                () -> assertThat(capabilities.getAggregateFunctionCapabilities(),
                        containsInAnyOrder(aggregateFunctionCapabilities)));
    }

    @Test
    void subtractFromEmptyCapabilities() {
        final Capabilities emptyCapabilities = this.builder.build();
        final Capabilities capabilitiesToExclude = this.builder.addMain(MainCapability.AGGREGATE_GROUP_BY_COLUMN)
                .addLiteral(LiteralCapability.DATE).build();

        final Capabilities result = emptyCapabilities.subtract(capabilitiesToExclude);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("deprecation")
    void subtractCapabilitiesDelegatesToSubtract() {
        final Capabilities capabilities = this.builder.addMain(MainCapability.AGGREGATE_GROUP_BY_COLUMN)
                .addLiteral(LiteralCapability.DATE).build();
        final Capabilities capabilitiesToExclude = Capabilities.builder()
                .addMain(MainCapability.AGGREGATE_GROUP_BY_COLUMN).build();

        final Capabilities result = capabilities.subtractCapabilities(capabilitiesToExclude);

        assertAll(() -> assertEmptyMainCapabilities(result),
                () -> assertThat(result.getLiteralCapabilities(), containsInAnyOrder(LiteralCapability.DATE)),
                () -> assertThat(capabilities.getMainCapabilities(),
                        containsInAnyOrder(MainCapability.AGGREGATE_GROUP_BY_COLUMN)),
                () -> assertThat(capabilities.getLiteralCapabilities(), containsInAnyOrder(LiteralCapability.DATE)));
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Capabilities.class).verify();
    }
}
