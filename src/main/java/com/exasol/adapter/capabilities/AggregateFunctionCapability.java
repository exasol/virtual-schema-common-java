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
    COUNT_DISTINCT(AggregateFunction.COUNT),
    /**
     * Count tuple aggregate function capability.
     */
    COUNT_TUPLE(AggregateFunction.COUNT),
    /**
     * Sum aggregate function capability.
     */
    SUM,
    /**
     * Sum distinct aggregate function capability.
     */
    SUM_DISTINCT(AggregateFunction.SUM),
    /**
     * Min aggregate function capability.
     */
    MIN,
    /**
     * Max aggregate function capability.
     */
    MAX,
    /**
     * Avg aggregate function capability.
     */
    AVG,
    /**
     * Avg distinct aggregate function capability.
     */
    AVG_DISTINCT(AggregateFunction.AVG),
    /**
     * Median aggregate function capability.
     */
    MEDIAN,
    /**
     * First value aggregate function capability.
     */
    FIRST_VALUE,
    /**
     * Last value aggregate function capability.
     */
    LAST_VALUE,
    /**
     * Stddev aggregate function capability.
     */
    STDDEV,
    /**
     * Stddev distinct aggregate function capability.
     */
    STDDEV_DISTINCT(AggregateFunction.STDDEV),
    /**
     * Stddev pop aggregate function capability.
     */
    STDDEV_POP,
    /**
     * Stddev pop distinct aggregate function capability.
     */
    STDDEV_POP_DISTINCT(AggregateFunction.STDDEV_POP),
    /**
     * Stddev samp aggregate function capability.
     */
    STDDEV_SAMP,
    /**
     * Stddev samp distinct aggregate function capability.
     */
    STDDEV_SAMP_DISTINCT(AggregateFunction.STDDEV_SAMP),
    /**
     * Variance aggregate function capability.
     */
    VARIANCE,
    /**
     * Variance distinct aggregate function capability.
     */
    VARIANCE_DISTINCT(AggregateFunction.VARIANCE),
    /**
     * Var pop aggregate function capability.
     */
    VAR_POP,
    /**
     * Var pop distinct aggregate function capability.
     */
    VAR_POP_DISTINCT(AggregateFunction.VAR_POP),
    /**
     * Var samp aggregate function capability.
     */
    VAR_SAMP,
    /**
     * Var samp distinct aggregate function capability.
     */
    VAR_SAMP_DISTINCT(AggregateFunction.VAR_SAMP),
    /**
     * Group concat aggregate function capability.
     */
    GROUP_CONCAT,
    /**
     * Group concat distinct aggregate function capability.
     */
    GROUP_CONCAT_DISTINCT(AggregateFunction.GROUP_CONCAT),
    /**
     * Group concat separator aggregate function capability.
     */
    GROUP_CONCAT_SEPARATOR(AggregateFunction.GROUP_CONCAT),
    /**
     * Group concat order by aggregate function capability.
     */
    GROUP_CONCAT_ORDER_BY(AggregateFunction.GROUP_CONCAT),
    /**
     * Geo intersection aggregate aggregate function capability.
     * @deprecated The {@code FN_AGG_GEO_INTERSECTION} capability was renamed to {@code FN_AGG_ST_INTERSECTION} in
     * Exasol 7.1.alpha1.
     */
    @Deprecated(since = "Exasol 7.1.alpha1")
    GEO_INTERSECTION_AGGREGATE,
    /**
     * Geo union aggregate aggregate function capability.
     * @deprecated The {@code FN_AGG_GEO_UNION} capability was renamed to {@code FN_AGG_ST_UNION} in Exasol 7.1.alpha1.
     */
    @Deprecated(since = "Exasol 7.1.alpha1")
    GEO_UNION_AGGREGATE,
    /**
     * Geometric intersection aggregate function capability.
     */
    ST_INTERSECTION,
    /**
     * St union aggregate function capability.
     */
    ST_UNION,
    /**
     * Approximate count distinct aggregate function capability.
     */
    APPROXIMATE_COUNT_DISTINCT,
    /**
     * Mul aggregate function capability.
     */
    MUL,
    /**
     * Mul distinct aggregate function capability.
     */
    MUL_DISTINCT(AggregateFunction.MUL),
    /**
     * Every aggregate function capability.
     */
    EVERY,
    /**
     * Some aggregate function capability.
     */
    SOME,
    /**
     * Listagg aggregate function capability.
     */
    LISTAGG,
    /**
     * Listagg distinct aggregate function capability.
     */
    LISTAGG_DISTINCT(AggregateFunction.LISTAGG),
    /**
     * Listagg separator aggregate function capability.
     */
    LISTAGG_SEPARATOR(AggregateFunction.LISTAGG),
    /**
     * Listagg on overflow error aggregate function capability.
     */
    LISTAGG_ON_OVERFLOW_ERROR(AggregateFunction.LISTAGG),
    /**
     * Listagg on overflow truncate aggregate function capability.
     */
    LISTAGG_ON_OVERFLOW_TRUNCATE(AggregateFunction.LISTAGG),
    /**
     * Listagg order by aggregate function capability.
     */
    LISTAGG_ORDER_BY(AggregateFunction.LISTAGG);

    private final AggregateFunction function;

    AggregateFunctionCapability() {
        this.function = AggregateFunction.valueOf(this.name());
    }

    AggregateFunctionCapability(final AggregateFunction function) {
        this.function = function;
    }

    /**
     * Gets function.
     *
     * @return the function
     */
    public AggregateFunction getFunction() {
        return this.function;
    }
}