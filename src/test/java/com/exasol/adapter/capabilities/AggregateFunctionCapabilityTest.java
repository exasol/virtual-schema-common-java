package com.exasol.adapter.capabilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.sql.AggregateFunction;

class AggregateFunctionCapabilityTest {
    @Test
    void testIfThereAreFunctionsWhereCapabilitiesMissing() {
        for (final AggregateFunction func : AggregateFunction.values()) {
            boolean foundCap = false;
            for (final AggregateFunctionCapability cap : AggregateFunctionCapability.values()) {
                if (cap.getFunction() == func) {
                    foundCap = true;
                }
            }
            assertTrue(foundCap, "Did not find a capability for function " + func.name());
        }
    }

    @Test
    void testConsistentNaming() {
        for (final AggregateFunctionCapability cap : AggregateFunctionCapability.values()) {
            assertTrue(cap.name().startsWith(cap.getFunction().name()));
        }
    }
}