package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.AggregateFunction;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class AggregateFunctionCapabilityTest {
    @Test
    void testCompleteness() {
        // Do we have functions where we don't have capabilities for?
        for (final AggregateFunction func : AggregateFunction.values()) {
            boolean foundCap = false;
            for (final AggregateFunctionCapability cap : AggregateFunctionCapability.values()) {
                if (cap.getFunction() == func) {
                    foundCap = true;
                }
            }
            assertTrue("Did not find a capability for function " + func.name(), foundCap);
        }
    }

    @Test
    void testConsistentNaming() {
        for (final AggregateFunctionCapability cap : AggregateFunctionCapability.values()) {
            assertTrue(cap.name().startsWith(cap.getFunction().name()));
        }
    }

}