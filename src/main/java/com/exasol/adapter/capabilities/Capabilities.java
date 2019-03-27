package com.exasol.adapter.capabilities;

import java.util.*;

/**
 * Manages a set of supported capabilities
 */
public final class Capabilities {
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
     * Removes unsupported capabilities
     *
     * @param capabilitiesToExclude unsupported capabilities
     * @return supported capabilities
     */
    public Capabilities subtractCapabilities(final Capabilities capabilitiesToExclude) {
        final Builder builder = builder();
        final Set<MainCapability> mainCapabilitiesWithExclusions = this.mainCapabilities;
        mainCapabilitiesWithExclusions.removeAll(capabilitiesToExclude.getMainCapabilities());
        final Set<LiteralCapability> literalCapabilitiesWithExclusions = this.literalCapabilities;
        literalCapabilitiesWithExclusions.removeAll(capabilitiesToExclude.getLiteralCapabilities());
        final Set<PredicateCapability> predicateCapabilitiesWithExclusions = this.predicateCapabilities;
        predicateCapabilitiesWithExclusions.removeAll(capabilitiesToExclude.getPredicateCapabilities());
        final Set<ScalarFunctionCapability> scalarCapabilitiesWithExclusions = this.scalarFunctionCapabilities;
        scalarCapabilitiesWithExclusions.removeAll(capabilitiesToExclude.getScalarFunctionCapabilities());
        final Set<AggregateFunctionCapability> aggregateCapabilitiesWithExclusions = this.aggregateFunctionCapabilities;
        aggregateCapabilitiesWithExclusions.removeAll(capabilitiesToExclude.getAggregateFunctionCapabilities());

        builder.addMain(mainCapabilitiesWithExclusions).addPredicate(predicateCapabilitiesWithExclusions)
                .addLiteral(literalCapabilitiesWithExclusions).addScalarFunction(scalarCapabilitiesWithExclusions)
                .addAggregateFunction(aggregateCapabilitiesWithExclusions);
        return builder.build();
    }

    /**
     * @return <code>true</code> if the object does not contain any capabilities
     */
    public boolean isEmpty() {
        return this.mainCapabilities.isEmpty() && this.literalCapabilities.isEmpty()
                && this.predicateCapabilities.isEmpty() && this.scalarFunctionCapabilities.isEmpty()
                && this.aggregateFunctionCapabilities.isEmpty();
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
            this.mainCapabilities.addAll(Arrays.asList(capabilities));
            return this;
        }

        /**
         * Add one or more main capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addMain(final Set<MainCapability> capabilities) {
            this.mainCapabilities.addAll(capabilities);
            return this;
        }

        /**
         * Add one or more literal capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addLiteral(final LiteralCapability... capabilities) {
            this.literalCapabilities.addAll(Arrays.asList(capabilities));
            return this;
        }

        /**
         * Add one or more literal capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addLiteral(final Set<LiteralCapability> capabilities) {
            this.literalCapabilities.addAll(capabilities);
            return this;
        }

        /**
         * Add one or more predicate capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addPredicate(final PredicateCapability... capabilities) {
            this.predicateCapabilities.addAll(Arrays.asList(capabilities));
            return this;
        }

        /**
         * Add one or more predicate capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addPredicate(final Set<PredicateCapability> capabilities) {
            this.predicateCapabilities.addAll(capabilities);
            return this;
        }

        /**
         * Add one or more scalar function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addScalarFunction(final ScalarFunctionCapability... capabilities) {
            this.scalarFunctionCapabilities.addAll(Arrays.asList(capabilities));
            return this;
        }

        /**
         * Add one or more scalar function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addScalarFunction(final Set<ScalarFunctionCapability> capabilities) {
            this.scalarFunctionCapabilities.addAll(capabilities);
            return this;
        }

        /**
         * Add one or more aggregate function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addAggregateFunction(final AggregateFunctionCapability... capabilities) {
            this.aggregateFunctionCapabilities.addAll(Arrays.asList(capabilities));
            return this;
        }

        /**
         * Add one or more aggregate function capabilities
         *
         * @param capabilities capabilities to be added
         * @return builder instance for fluent programming
         */
        public Builder addAggregateFunction(final Set<AggregateFunctionCapability> capabilities) {
            this.aggregateFunctionCapabilities.addAll(capabilities);
            return this;
        }
    }
}