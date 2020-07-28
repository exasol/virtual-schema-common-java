package com.exasol.adapter.capabilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.sql.Predicate;

class PredicateCapabilityTest {
    @Test
    void testIfThereArePredicatesWhereCapabilitiesMissing() {
        for (final Predicate pred : Predicate.values()) {
            boolean foundCap = false;
            for (final PredicateCapability cap : PredicateCapability.values()) {
                if (cap.getPredicate() == pred) {
                    foundCap = true;
                }
            }
            assertTrue(foundCap, "Did not find a capability for predicate " + pred.name());
        }
    }

    @Test
    void testConsistentNaming() {
        for (final PredicateCapability cap : PredicateCapability.values()) {
            assertTrue(cap.name().startsWith(cap.getPredicate().name()));
        }
    }
}