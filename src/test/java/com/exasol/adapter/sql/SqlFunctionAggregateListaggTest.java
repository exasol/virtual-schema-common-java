package com.exasol.adapter.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class SqlFunctionAggregateListaggTest {
    @Test
    void testIllegalTruncationType() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SqlFunctionAggregateListagg.Behavior.TruncationType.parseTruncationType("SOME STRING"));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-25"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "WITH COUNT", "WITHOUT COUNT" })
    void testTruncationTypeParsing(final String value) {
        assertDoesNotThrow(() -> SqlFunctionAggregateListagg.Behavior.TruncationType.parseTruncationType(value));
    }

    @Test
    void testGetChildren() {
        final SqlNode argument = new SqlLiteralString("test string");
        final SqlFunctionAggregateListagg.Behavior behaviour = new SqlFunctionAggregateListagg.Behavior(SqlFunctionAggregateListagg.BehaviorType.TRUNCATE);
        final SqlFunctionAggregateListagg listagg = SqlFunctionAggregateListagg.builder(argument, behaviour).build();
        assertThat(listagg.getChildren(), equalTo(List.of(argument)));
    }
}