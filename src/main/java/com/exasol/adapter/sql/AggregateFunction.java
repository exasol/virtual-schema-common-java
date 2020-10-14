package com.exasol.adapter.sql;

/**
 * List of all aggregation functions supported by EXASOL.
 */
public enum AggregateFunction {
    COUNT, SUM, MIN, MAX, AVG, MEDIAN, FIRST_VALUE, LAST_VALUE, STDDEV, STDDEV_POP, STDDEV_SAMP, VARIANCE, VAR_POP,
    VAR_SAMP, GROUP_CONCAT(false, "GROUP_CONCAT"), APPROXIMATE_COUNT_DISTINCT,
    GEO_INTERSECTION_AGGREGATE(true, "ST_INTERSECTION"), GEO_UNION_AGGREGATE(true, "ST_UNION"), ST_INTERSECTION,
    ST_UNION, MUL, EVERY, SOME, LISTAGG(false, "LISTAGG");

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
        if (realFunctionName != null && !realFunctionName.isBlank()) {
            return realFunctionName;
        } else {
            return name();
        }
    }
}