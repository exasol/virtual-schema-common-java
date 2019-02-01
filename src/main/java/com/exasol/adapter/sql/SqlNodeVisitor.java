package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Implementation of the Visitor pattern for the SqlNode.
 * 
 * Benefit of this Visitor implementation: We get compile time safety that all
 * Visitors have implementations for all SqlNode types.
 * 
 * Drawback of this Visitor implementation: Whenever a new SqlNode gets added,
 * we need to implement it here (should be fine for now). If this becomes to
 * annoying, we can still switch to a visitor pattern using Reflection.
 */
public interface SqlNodeVisitor<R> {

    R visit(SqlStatementSelect select) throws AdapterException;

    R visit(SqlSelectList selectList) throws AdapterException;

    R visit(SqlGroupBy groupBy) throws AdapterException;

    R visit(SqlColumn sqlColumn) throws AdapterException;

    R visit(SqlFunctionAggregate sqlFunctionAggregate) throws AdapterException;

    R visit(SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat) throws AdapterException;

    R visit(SqlFunctionScalar sqlFunctionScalar) throws AdapterException;

    R visit(SqlFunctionScalarCase sqlFunctionScalarCase) throws AdapterException;

    R visit(SqlFunctionScalarCast sqlFunctionScalarCast) throws AdapterException;

    R visit(SqlFunctionScalarExtract sqlFunctionScalarExtract) throws AdapterException;

    R visit(SqlLimit sqlLimit) throws AdapterException;

    R visit(SqlLiteralBool sqlLiteralBool) throws AdapterException;

    R visit(SqlLiteralDate sqlLiteralDate) throws AdapterException;

    R visit(SqlLiteralDouble sqlLiteralDouble) throws AdapterException;

    R visit(SqlLiteralExactnumeric sqlLiteralExactnumeric) throws AdapterException;

    R visit(SqlLiteralNull sqlLiteralNull) throws AdapterException;

    R visit(SqlLiteralString sqlLiteralString) throws AdapterException;

    R visit(SqlLiteralTimestamp sqlLiteralTimestamp) throws AdapterException;

    R visit(SqlLiteralTimestampUtc sqlLiteralTimestampUtc) throws AdapterException;

    R visit(SqlLiteralInterval sqlLiteralInterval) throws AdapterException;

    R visit(SqlOrderBy sqlOrderBy) throws AdapterException;

    R visit(SqlPredicateAnd sqlPredicateAnd) throws AdapterException;

    R visit(SqlPredicateBetween sqlPredicateBetween) throws AdapterException;

    R visit(SqlPredicateEqual sqlPredicateEqual) throws AdapterException;

    R visit(SqlPredicateInConstList sqlPredicateInConstList) throws AdapterException;

    R visit(SqlPredicateLess sqlPredicateLess) throws AdapterException;

    R visit(SqlPredicateLessEqual sqlPredicateLessEqual) throws AdapterException;

    R visit(SqlPredicateLike sqlPredicateLike) throws AdapterException;

    R visit(SqlPredicateLikeRegexp sqlPredicateLikeRegexp) throws AdapterException;

    R visit(SqlPredicateNot sqlPredicateNot) throws AdapterException;

    R visit(SqlPredicateNotEqual sqlPredicateNotEqual) throws AdapterException;

    R visit(SqlPredicateOr sqlPredicateOr) throws AdapterException;

    R visit(SqlPredicateIsNotNull sqlPredicateOr) throws AdapterException;

    R visit(SqlPredicateIsNull sqlPredicateOr) throws AdapterException;

    R visit(SqlTable sqlTable) throws AdapterException;

    R visit(SqlJoin sqlJoin) throws AdapterException;

}
