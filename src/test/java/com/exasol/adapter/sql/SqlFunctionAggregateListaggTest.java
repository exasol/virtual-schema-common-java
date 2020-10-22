package com.exasol.adapter.sql;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SqlFunctionAggregateListaggTest {
    @Test
    void testIllegalTruncationType() {
        assertThrows(IllegalArgumentException.class,
                () -> SqlFunctionAggregateListagg.Behavior.TruncationType.parseTruncationType("SOME STRING"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "WITH COUNT", "WITHOUT COUNT" })
    void testTruncationTypeParsing(String value) {
        assertDoesNotThrow(() -> SqlFunctionAggregateListagg.Behavior.TruncationType.parseTruncationType(value));
    }
}