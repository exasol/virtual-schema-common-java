package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.AggregateFunction;

/**
 * List of all aggregation function capabilities supported by EXASOL.
 */
public enum AggregateFunctionCapability {
    /**
     * Required for any kind of COUNT(...) with expressions.
     */
    COUNT,
    /**
     * Required only for COUNT(*).
     */
    COUNT_STAR(AggregateFunction.COUNT),
    /**
     * Required for COUNT(DISTINCT ...).
     */
    COUNT_DISTINCT(AggregateFunction.COUNT), COUNT_TUPLE(AggregateFunction.COUNT), SUM,
    SUM_DISTINCT(AggregateFunction.SUM), MIN, MAX, AVG, AVG_DISTINCT(AggregateFunction.AVG), MEDIAN, FIRST_VALUE,
    LAST_VALUE, STDDEV, STDDEV_DISTINCT(AggregateFunction.STDDEV), STDDEV_POP,
    STDDEV_POP_DISTINCT(AggregateFunction.STDDEV_POP), STDDEV_SAMP, STDDEV_SAMP_DISTINCT(AggregateFunction.STDDEV_SAMP),
    VARIANCE, VARIANCE_DISTINCT(AggregateFunction.VARIANCE), VAR_POP, VAR_POP_DISTINCT(AggregateFunction.VAR_POP),
    VAR_SAMP, VAR_SAMP_DISTINCT(AggregateFunction.VAR_SAMP), GROUP_CONCAT,
    GROUP_CONCAT_DISTINCT(AggregateFunction.GROUP_CONCAT), GROUP_CONCAT_SEPARATOR(AggregateFunction.GROUP_CONCAT),
    GROUP_CONCAT_ORDER_BY(AggregateFunction.GROUP_CONCAT), GEO_INTERSECTION_AGGREGATE, GEO_UNION_AGGREGATE,
    ST_INTERSECTION, ST_UNION, APPROXIMATE_COUNT_DISTINCT, MUL, MUL_DISTINCT(AggregateFunction.MUL), EVERY, SOME,
    LISTAGG, LISTAGG_DISTINCT(AggregateFunction.LISTAGG), LISTAGG_SEPARATOR(AggregateFunction.LISTAGG),
    LISTAGG_ON_OVERFLOW_ERROR(AggregateFunction.LISTAGG), LISTAGG_ON_OVERFLOW_TRUNCATE(AggregateFunction.LISTAGG),
    LISTAGG_ORDER_BY(AggregateFunction.LISTAGG);

    private final AggregateFunction function;

    AggregateFunctionCapability() {
        this.function = AggregateFunction.valueOf(this.name());
    }

    AggregateFunctionCapability(final AggregateFunction function) {
        this.function = function;
    }

    public AggregateFunction getFunction() {
        return this.function;
    }
}