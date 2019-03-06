package com.exasol.adapter.capabilities;

import java.util.EnumSet;
import java.util.Set;

/**
 * Manages a set of supported capabilities
 */
public class Capabilities {
    private final Set<MainCapability> mainCapabilities;
    private final Set<LiteralCapability> literalCapabilities;
    private final Set<PredicateCapability> predicateCapabilities;
    private final Set<ScalarFunctionCapability> scalarFunctionCapabilities;
    private final Set<AggregateFunctionCapability> aggregateFunctionCapabilities;

    private Capabilities(final Builder builder) {
        this.mainCapabilities = EnumSet.copyOf(builder.mainCapabilities);
        this.literalCapabilities = EnumSet.copyOf(builder.literalCapabilities);
        this.predicateCapabilities = EnumSet.copyOf(builder.predicateCapabilities);
        this.scalarFunctionCapabilities = EnumSet.copyOf(builder.scalarFunctionCapabilities);
        this.aggregateFunctionCapabilities = EnumSet.copyOf(builder.aggregateFunctionCapabilities);
    }

    /**
     * Get the Virtual Schema's adapters main capabilities
     *
     * @return main capabilities
     */
    public Set<MainCapability> getMainCapabilities() {
        return this.mainCapabilities;
    }

    /**
     * Get the Virtual Schema's adapters literal capabilities
     *
     * @return scalar literal capabilities
     */
    public Set<LiteralCapability> getLiteralCapabilities() {
        return this.literalCapabilities;
    }

    /**
     * Get the Virtual Schema's adapters predicate capabilities
     *
     * @return predicate capabilities
     */
    public Set<PredicateCapability> getPredicateCapabilities() {
        return this.predicateCapabilities;
    }

    /**
     * Get the Virtual Schema's adapters scalar function capabilities
     *
     * @return scalar function capabilities
     */
    public Set<ScalarFunctionCapability> getScalarFunctionCapabilities() {
        return this.scalarFunctionCapabilities;
    }

    /**
     * Get the Virtual Schema's adapters aggregate function capabilities
     *
     * @return aggregate function capabilities
     */
    public Set<AggregateFunctionCapability> getAggregateFunctionCapabilities() {
        return this.aggregateFunctionCapabilities;
    }

    /**
     * Get a {@link Capabilities} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Capabilities}
     */
    public static final class Builder {
        final Set<MainCapability> mainCapabilities = EnumSet.noneOf(MainCapability.class);
        final Set<LiteralCapability> literalCapabilities = EnumSet.noneOf(LiteralCapability.class);
        final Set<PredicateCapability> predicateCapabilities = EnumSet.noneOf(PredicateCapability.class);
        final Set<ScalarFunctionCapability> scalarFunctionCapabilities = EnumSet.noneOf(ScalarFunctionCapability.class);
        final Set<AggregateFunctionCapability> aggregateFunctionCapabilities = EnumSet
                .noneOf(AggregateFunctionCapability.class);

        /**
         * Create new capability instance
         *
         * @return new capability instance
         */
        public Capabilities build() {
            return new Capabilities(this);
        }

        /**
         * Add one or more main capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addMain(final MainCapability... capabilities) {
            for (final MainCapability capability : capabilities) {
                this.mainCapabilities.add(capability);
            }
            return this;
        }

        /**
         * Add one or more literal capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addLiteral(final LiteralCapability... capabilities) {
            for (final LiteralCapability capability : capabilities) {
                this.literalCapabilities.add(capability);
            }
            return this;
        }

        /**
         * Add one or more predicate capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addPredicate(final PredicateCapability... capabilities) {
            for (final PredicateCapability capability : capabilities) {
                this.predicateCapabilities.add(capability);
            }
            return this;
        }

        /**
         * Add one or more scalar function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addScalarFunction(final ScalarFunctionCapability... capabilities) {
            for (final ScalarFunctionCapability capability : capabilities) {
                this.scalarFunctionCapabilities.add(capability);
            }
            return this;
        }

        /**
         * Add one or more aggregate function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addAggregateFunction(final AggregateFunctionCapability... capabilities) {
            for (final AggregateFunctionCapability capability : capabilities) {
                this.aggregateFunctionCapabilities.add(capability);
            }
            return this;
        }
    }
}