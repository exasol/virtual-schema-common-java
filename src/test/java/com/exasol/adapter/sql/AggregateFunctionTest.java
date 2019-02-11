package com.exasol.adapter.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AggregateFunctionTest {
    @Test
    void testIsSimpleFalse() {
        assertFalse(AggregateFunction.GROUP_CONCAT.isSimple());
    }

    @Test
    void testIsSimpleTrue() {
        assertTrue(AggregateFunction.COUNT.isSimple());
    }
}