package com.exasol.adapter.capabilities;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages a set of supported Capabilities
 */
public class Capabilities {
    private final Set<MainCapability> mainCapabilities = new HashSet<>();
    private final Set<ScalarFunctionCapability> scalarFunctionCaps = new HashSet<>();
    private final Set<PredicateCapability> predicateCaps = new HashSet<>();
    private final Set<AggregateFunctionCapability> aggregateFunctionCaps = new HashSet<>();
    private final Set<LiteralCapability> literalCaps = new HashSet<>();

    public void supportAllCapabilities() {
        for (final MainCapability cap : MainCapability.values()) {
            supportMainCapability(cap);
        }
        for (final ScalarFunctionCapability function : ScalarFunctionCapability.values()) {
            supportScalarFunction(function);
        }
        for (final PredicateCapability pred : PredicateCapability.values()) {
            supportPredicate(pred);
        }
        for (final AggregateFunctionCapability function : AggregateFunctionCapability.values()) {
            supportAggregateFunction(function);
        }
        for (final LiteralCapability cap : LiteralCapability.values()) {
            supportLiteral(cap);
        }
    }

    public void subtractCapabilities(final Capabilities capabilitiesToSubtract) {
        for (final MainCapability cap : capabilitiesToSubtract.mainCapabilities) {
            mainCapabilities.remove(cap);
        }
        for (final ScalarFunctionCapability cap : capabilitiesToSubtract.getScalarFunctionCapabilities()) {
            scalarFunctionCaps.remove(cap);
        }
        for (final PredicateCapability cap : capabilitiesToSubtract.getPredicateCapabilities()) {
            predicateCaps.remove(cap);
        }
        for (final AggregateFunctionCapability cap : capabilitiesToSubtract.getAggregateFunctionCapabilities()) {
            aggregateFunctionCaps.remove(cap);
        }
        for (final LiteralCapability cap : capabilitiesToSubtract.getLiteralCapabilities()) {
            literalCaps.remove(cap);
        }
    }

    public void supportMainCapability(final MainCapability cap) {
        mainCapabilities.add(cap);
    }

    public void supportScalarFunction(final ScalarFunctionCapability functionType) {
        scalarFunctionCaps.add(functionType);
    }

    public void supportPredicate(final PredicateCapability predicate) {
        predicateCaps.add(predicate);
    }

    public void supportAggregateFunction(final AggregateFunctionCapability functionType) {
        aggregateFunctionCaps.add(functionType);
    }

    public void supportLiteral(final LiteralCapability literal) {
        literalCaps.add(literal);
    }

    public Set<MainCapability> getMainCapabilities() {
        return mainCapabilities;
    }

    public Set<ScalarFunctionCapability> getScalarFunctionCapabilities() {
        return scalarFunctionCaps;
    }

    public Set<PredicateCapability> getPredicateCapabilities() {
        return predicateCaps;
    }

    public Set<AggregateFunctionCapability> getAggregateFunctionCapabilities() {
        return aggregateFunctionCaps;
    }

    public Set<LiteralCapability> getLiteralCapabilities() {
        return literalCaps;
    }
}
