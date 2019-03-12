package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.ScalarFunction;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            assertTrue("Did not find a capability for function " + function.name(), foundCap);
        }
    }

    @Test
    void testConsistentNaming() {
        for (final ScalarFunctionCapability cap : ScalarFunctionCapability.values()) {
            assertEquals(cap.name(), cap.getFunction().name());
        }
    }

}