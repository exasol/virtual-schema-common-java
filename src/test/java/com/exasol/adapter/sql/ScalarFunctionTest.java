package com.exasol.adapter.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScalarFunctionTest {
    @Test
    void testIsSimpleFalse() {
        assertFalse(ScalarFunction.EXTRACT.isSimple());
    }

    @Test
    void testIsSimpleTrue() {
        assertTrue(ScalarFunction.ABS.isSimple());
    }
}