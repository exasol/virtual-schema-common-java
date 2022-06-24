package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Implementation of the Visitor pattern for the SqlNode.
 * <p>
 * Benefit of this Visitor implementation: We get compile time safety that all Visitors have implementations for all
 * SqlNode types.
 * <p>
 * Drawback of this Visitor implementation: Whenever a new SqlNode gets added, we need to implement it here (should be
 * fine for now). If this becomes to annoying, we can still switch to a visitor pattern using Reflection.
 *
 * @param <R> node being visited
 */
public interface SqlNodeVisitor<R> {

    /**
     * Visit.
     *
     * @param select the select
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlStatementSelect select) throws AdapterException;

    /**
     * Visit.
     *
     * @param selectList the select list
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlSelectList selectList) throws AdapterException;

    /**
     * Visit.
     *
     * @param groupBy the group by
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlGroupBy groupBy) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlColumn the sql column
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlColumn sqlColumn) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionAggregate the sql function aggregate
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionAggregate sqlFunctionAggregate) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionAggregateGroupConcat the sql function aggregate group concat
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionScalar the sql function scalar
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionScalar sqlFunctionScalar) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionScalarCase the sql function scalar case
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionScalarCase sqlFunctionScalarCase) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionScalarCast the sql function scalar cast
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionScalarCast sqlFunctionScalarCast) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionScalarExtract the sql function scalar extract
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionScalarExtract sqlFunctionScalarExtract) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionScalarJsonValue the sql function scalar json value
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionScalarJsonValue sqlFunctionScalarJsonValue) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLimit the sql limit
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLimit sqlLimit) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralBool the sql literal bool
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralBool sqlLiteralBool) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralDate the sql literal date
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralDate sqlLiteralDate) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralDouble the sql literal double
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralDouble sqlLiteralDouble) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralExactnumeric the sql literal exactnumeric
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralExactnumeric sqlLiteralExactnumeric) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralNull the sql literal null
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralNull sqlLiteralNull) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralString the sql literal string
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralString sqlLiteralString) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralTimestamp the sql literal timestamp
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralTimestamp sqlLiteralTimestamp) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralTimestampUtc the sql literal timestamp utc
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralTimestampUtc sqlLiteralTimestampUtc) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlLiteralInterval the sql literal interval
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlLiteralInterval sqlLiteralInterval) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlOrderBy the sql order by
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlOrderBy sqlOrderBy) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateAnd the sql predicate and
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateAnd sqlPredicateAnd) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateBetween the sql predicate between
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateBetween sqlPredicateBetween) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateEqual the sql predicate equal
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateEqual sqlPredicateEqual) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateInConstList the sql predicate in const list
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateInConstList sqlPredicateInConstList) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateIsJson the sql predicate is json
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateIsJson sqlPredicateIsJson) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateIsNotJson the sql predicate is not json
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateIsNotJson sqlPredicateIsNotJson) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateLess the sql predicate less
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateLess sqlPredicateLess) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateLessEqual the sql predicate less equal
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateLessEqual sqlPredicateLessEqual) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateLike the sql predicate like
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateLike sqlPredicateLike) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateLikeRegexp the sql predicate like regexp
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateLikeRegexp sqlPredicateLikeRegexp) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateNot the sql predicate not
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateNot sqlPredicateNot) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateNotEqual the sql predicate not equal
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateNotEqual sqlPredicateNotEqual) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateOr the sql predicate or
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateOr sqlPredicateOr) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateOr the sql predicate or
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateIsNotNull sqlPredicateOr) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlPredicateOr the sql predicate or
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlPredicateIsNull sqlPredicateOr) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlTable the sql table
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlTable sqlTable) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlJoin the sql join
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlJoin sqlJoin) throws AdapterException;

    /**
     * Visit.
     *
     * @param sqlFunctionAggregateListagg the sql function aggregate listagg
     * @return the result
     * @throws AdapterException the adapter exception
     */
    public R visit(SqlFunctionAggregateListagg sqlFunctionAggregateListagg) throws AdapterException;
}