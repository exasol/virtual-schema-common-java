package com.exasol.adapter.sql;

/**
 * List of all aggregation functions supported by EXASOL.
 */
public enum AggregateFunction {
    /**
     * Count aggregate function.
     */
    COUNT,
    /**
     * Sum aggregate function.
     */
    SUM,
    /**
     * Min aggregate function.
     */
    MIN,
    /**
     * Max aggregate function.
     */
    MAX,
    /**
     * Avg aggregate function.
     */
    AVG,
    /**
     * Median aggregate function.
     */
    MEDIAN,
    /**
     * First value aggregate function.
     */
    FIRST_VALUE,
    /**
     * Last value aggregate function.
     */
    LAST_VALUE,
    /**
     * Stddev aggregate function.
     */
    STDDEV,
    /**
     * Stddev pop aggregate function.
     */
    STDDEV_POP,
    /**
     * Stddev samp aggregate function.
     */
    STDDEV_SAMP,
    /**
     * Variance aggregate function.
     */
    VARIANCE,
    /**
     * Var pop aggregate function.
     */
    VAR_POP,
    /**
     * Var samp aggregate function.
     */
    VAR_SAMP,
    /**
     * Group concat aggregate function.
     */
    GROUP_CONCAT(false, "GROUP_CONCAT"),
    /**
     * Approximate count distinct aggregate function.
     */
    APPROXIMATE_COUNT_DISTINCT,
    /**
     * Geo intersection aggregate aggregate function.
     */
    GEO_INTERSECTION_AGGREGATE(true, "ST_INTERSECTION"),
    /**
     * Geo union aggregate aggregate function.
     */
    GEO_UNION_AGGREGATE(true, "ST_UNION"),
    /**
     * St intersection aggregate function.
     */
    ST_INTERSECTION,
    /**
     * St union aggregate function.
     */
    ST_UNION,
    /**
     * Mul aggregate function.
     */
    MUL,
    /**
     * Every aggregate function.
     */
    EVERY,
    /**
     * Some aggregate function.
     */
    SOME,
    /**
     * Listagg aggregate function.
     */
    LISTAGG(false, "LISTAGG");

    private final boolean simple;
    private String realFunctionName;

    /**
     * True if the function is simple, i.e. is handled by {@link SqlFunctionAggregate}, and false if it has it's own
     * implementation.
     *
     * @return <code>true</code> if the function is simple
     */
    public boolean isSimple() {
        return this.simple;
    }

    AggregateFunction() {
        this.simple = true;
    }

    AggregateFunction(final boolean simple, final String realFunctionName) {
        this.simple = simple;
        this.realFunctionName = realFunctionName;
    }

    @Override
    public String toString() {
        if (this.realFunctionName != null && !this.realFunctionName.isBlank()) {
            return this.realFunctionName;
        } else {
            return name();
        }
    }
}