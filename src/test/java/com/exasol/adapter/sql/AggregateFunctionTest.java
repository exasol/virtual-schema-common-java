package com.exasol.adapter.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AggregateFunctionTest {
    @Test
    void testIsSimpleFalse() {
        assertFalse(AggregateFunction.GROUP_CONCAT.isSimple());
    }

    @Test
    void testIsSimpleTrue() {
        assertTrue(AggregateFunction.COUNT.isSimple());
    }

    @Test
    void testToStringWithSpecialName() {
        assertThat(AggregateFunction.GEO_INTERSECTION_AGGREGATE.toString(), equalTo("ST_INTERSECTION"));
    }

    @Test
    void testToString() {
        assertThat(AggregateFunction.AVG.toString(), equalTo("AVG"));
    }
}