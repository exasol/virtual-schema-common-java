package com.exasol.adapter.capabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.sql.ScalarFunction;

class ScalarFunctionCapabilityTest {
    @Test
    void testCompleteness() {
        // Do we have functions where we don't have capabilities for?
        for (final ScalarFunction function : ScalarFunction.values()) {
            boolean foundCap = false;
            for (final ScalarFunctionCapability cap : ScalarFunctionCapability.values()) {
                if (cap.getFunction() == function) {
                    foundCap = true;
                }
            }
            assertTrue(foundCap, "Did not find a capability for function " + function.name());
        }
    }

    @Test
    void testConsistentNaming() {
        for (final ScalarFunctionCapability cap : ScalarFunctionCapability.values()) {
            assertEquals(cap.name(), cap.getFunction().name());
        }
    }
}