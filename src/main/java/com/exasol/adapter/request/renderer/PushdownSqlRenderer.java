package com.exasol.adapter.request.renderer;

import static com.exasol.adapter.request.RequestJsonKeys.*;

import java.util.List;
import java.util.Optional;

import javax.json.*;
import javax.json.spi.JsonProvider;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.sql.*;
import com.exasol.errorreporting.ExaError;

/**
 * This class serializes {@link SqlNode}s to JSON.
 * <p>
 * It's the inversion of {@link com.exasol.adapter.request.parser.PushdownSqlParser}.
 * </p>
 */
public class PushdownSqlRenderer {

    private static final JsonProvider JSON = JsonProvider.provider();

    /**
     * Serialize a {@link SqlNode} to JSON.
     *
     * @param node node to serialize
     * @return JSON representation
     */
    public JsonValue render(final SqlNode node) {
        try {
            return node.accept(new ConvertVisitor());
        } catch (final AdapterException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VS-COM-JAVA-34")
                    .message("n unexpected error occurred during request serialization.").ticketMitigation().toString(),
                    exception);
        }
    }

    private static class ConvertVisitor implements SqlNodeVisitor<JsonValue> {

        @Override
        public JsonObject visit(final SqlStatementSelect select) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(select);
            builder.add(FROM, select.getFromClause().accept(this));
            addIfPresent(select.getSelectList(), SELECT_LIST, builder);
            addIfPresent(select.getGroupBy(), GROUP_BY, builder);
            addIfPresent(select.getWhereClause(), FILTER, builder);
            addIfPresent(select.getHaving(), HAVING, builder);
            addIfPresent(select.getOrderBy(), ORDER_BY, builder);
            addIfPresent(select.getLimit(), LIMIT, builder);
            return builder.build();
        }

        private JsonObjectBuilder createObjectBuilderFor(final SqlNode type) {
            return JSON.createObjectBuilder().add(TYPE, type.getType().name().toLowerCase());
        }

        private void addIfPresent(final SqlNode node, final String key, final JsonObjectBuilder builder)
                throws AdapterException {
            if (node != null) {
                builder.add(key, node.accept(this));
            }
        }

        @Override
        public JsonArray visit(final SqlSelectList selectList) throws AdapterException {
            return convertListOfNodes(selectList.getExpressions());
        }

        private JsonArray convertListOfNodes(final List<SqlNode> expressions) throws AdapterException {
            final JsonArrayBuilder resultBuilder = JSON.createArrayBuilder();
            for (final SqlNode node : expressions) {
                resultBuilder.add(node.accept(this));
            }
            return resultBuilder.build();
        }

        @Override
        public JsonArray visit(final SqlGroupBy groupBy) throws AdapterException {
            return convertListOfNodes(groupBy.getExpressions());
        }

        @Override
        public JsonObject visit(final SqlColumn sqlColumn) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlColumn);
            builder.add(COLUMN_NR, sqlColumn.getId());
            builder.add(NAME, sqlColumn.getName());
            builder.add(TABLE_NAME, sqlColumn.getTableName());
            if (sqlColumn.hasTableAlias()) {
                builder.add(TABLE_ALIAS, sqlColumn.getTableAlias());
            }
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionAggregate sqlFunctionAggregate) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlFunctionAggregate);
            builder.add(NAME, sqlFunctionAggregate.getFunctionName());
            builder.add(ARGUMENTS, convertListOfNodes(sqlFunctionAggregate.getArguments()));
            if (sqlFunctionAggregate.hasDistinct()) {
                builder.add(DISTINCT, true);
            }
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionAggregateGroupConcat function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, function.getFunctionName());
            builder.add(ARGUMENTS, convertListOfNodes(List.of(function.getArgument())));
            if (function.hasDistinct()) {
                builder.add(DISTINCT, true);
            }
            addIfPresent(function.getOrderBy(), ORDER_BY, builder);
            builder.add(SEPARATOR, function.getSeparator().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionScalar function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, function.getFunctionName());
            builder.add(NUM_ARGS, function.getArguments().size());
            builder.add(ARGUMENTS, convertListOfNodes(function.getArguments()));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionScalarCase function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, "CASE");
            builder.add(ARGUMENTS, convertListOfNodes(function.getArguments()));
            builder.add(RESULTS, convertListOfNodes(function.getResults()));
            addIfPresent(function.getBasis(), BASIS, builder);
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionScalarCast function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(DATA_TYPE, render(function.getDataType()));
            builder.add(ARGUMENTS, convertListOfNodes(List.of(function.getArgument())));
            builder.add(NAME, "CAST");
            return builder.build();
        }

        private JsonObject render(final DataType dataType) {
            final JsonObjectBuilder builder = JSON.createObjectBuilder();
            final DataType.ExaDataType type = dataType.getExaDataType();
            builder.add(TYPE, type.name().toUpperCase());
            switch (type) {
            case DECIMAL:
                builder.add(PRECISION, dataType.getPrecision());
                builder.add(SCALE, dataType.getScale());
                break;
            case VARCHAR:
            case CHAR:
                builder.add(CHARACTER_SET, dataType.getCharset().name().toLowerCase());
                builder.add(SIZE, dataType.getSize());
                break;
            case TIMESTAMP:
                builder.add(WITH_LOCAL_TIME_ZONE, dataType.isWithLocalTimezone());
                break;
            case INTERVAL:
                builder.add(PRECISION, dataType.getPrecision());
                builder.add(FROM_TO, render(dataType.getIntervalType()));
                if (dataType.getIntervalType().equals(DataType.IntervalType.DAY_TO_SECOND)) {
                    builder.add(FRACTION, dataType.getIntervalFraction());
                }
                break;
            case GEOMETRY:
                builder.add(SRID, dataType.getGeometrySrid());
                break;
            default:
                break;
            }
            return builder.build();
        }

        private String render(final DataType.IntervalType intervalType) {
            switch (intervalType) {
            case DAY_TO_SECOND:
                return "DAY TO SECONDS";
            case YEAR_TO_MONTH:
                return "YEAR TO MONTH";
            default:
                throw new IllegalStateException(ExaError.messageBuilder("F-VS-COM-JAVA-35")
                        .message("Unimplemented interval type {{type}}.", intervalType).ticketMitigation().toString());
            }
        }

        @Override
        public JsonObject visit(final SqlFunctionScalarExtract function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, "EXTRACT");
            builder.add(TO_EXTRACT, function.getToExtract());
            builder.add(ARGUMENTS, convertListOfNodes(List.of(function.getArgument())));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionScalarJsonValue function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, function.getScalarFunction().name());
            builder.add(ARGUMENTS, convertListOfNodes(function.getArguments()));
            builder.add(RETURNING_DATA_TYPE, render(function.getReturningDataType()));
            builder.add(EMPTY_BEHAVIOR, render(function.getEmptyBehavior()));
            builder.add(ERROR_BEHAVIOR, render(function.getErrorBehavior()));
            return builder.build();
        }

        private JsonObject render(final SqlFunctionScalarJsonValue.Behavior behaviour) throws AdapterException {
            final JsonObjectBuilder builder = JSON.createObjectBuilder();
            builder.add(TYPE, behaviour.getBehaviorType());
            final Optional<SqlNode> expression = behaviour.getExpression();
            if (expression.isPresent()) {
                builder.add(EXPRESSION, expression.get().accept(this));
            }
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLimit sqlLimit) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLimit);
            builder.add(NUM_ELEMENTS, sqlLimit.getLimit());
            builder.add(OFFSET, sqlLimit.getOffset());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralBool sqlLiteralBool) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralBool);
            builder.add(VALUE, sqlLiteralBool.getValue());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralDate sqlLiteralDate) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralDate);
            builder.add(VALUE, sqlLiteralDate.getValue());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralDouble sqlLiteralDouble) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralDouble);
            builder.add(VALUE, String.valueOf(sqlLiteralDouble.getValue()));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralExactnumeric sqlLiteralExactnumeric) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralExactnumeric);
            builder.add(VALUE, sqlLiteralExactnumeric.getValue().toString());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralNull sqlLiteralNull) throws AdapterException {
            return createObjectBuilderFor(sqlLiteralNull).build();
        }

        @Override
        public JsonObject visit(final SqlLiteralString sqlLiteralString) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralString);
            builder.add(VALUE, sqlLiteralString.getValue());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralTimestamp sqlLiteralTimestamp) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralTimestamp);
            builder.add(VALUE, sqlLiteralTimestamp.getValue());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralTimestampUtc sqlLiteralTimestampUtc) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralTimestampUtc);
            builder.add(VALUE, sqlLiteralTimestampUtc.getValue());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlLiteralInterval sqlLiteralInterval) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlLiteralInterval);
            builder.add(VALUE, sqlLiteralInterval.getValue());
            builder.add(DATA_TYPE, render(sqlLiteralInterval.getDataType()));
            return builder.build();
        }

        @Override
        public JsonArray visit(final SqlOrderBy sqlOrderBy) throws AdapterException {
            final JsonArrayBuilder builder = JSON.createArrayBuilder();
            final List<Boolean> isAscendings = sqlOrderBy.isAscending();
            final List<SqlNode> expressions = sqlOrderBy.getExpressions();
            final List<Boolean> nullsLasts = sqlOrderBy.nullsLast();
            final int expressionsSize = expressions.size();
            if (expressionsSize != isAscendings.size() || expressionsSize != nullsLasts.size()) {
                throw new IllegalStateException(ExaError.messageBuilder("F-VS-COM-JAVA-33").message(
                        "Can not render SqlOrderBy as JSON because it has an invalid format. The size of the three lists must be equal.")
                        .ticketMitigation().toString());
            }
            for (int index = 0; index < expressionsSize; index++) {
                final JsonObjectBuilder objectBuilder = JSON.createObjectBuilder();
                objectBuilder.add(TYPE, ORDER_BY_ELEMENT);
                objectBuilder.add(EXPRESSION, expressions.get(index).accept(this));
                objectBuilder.add(IS_ASCENDING, isAscendings.get(index));
                objectBuilder.add(NULLS_LAST, nullsLasts.get(index));
                builder.add(objectBuilder);
            }
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateAnd sqlPredicateAnd) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateAnd);
            builder.add(EXPRESSIONS, convertListOfNodes(sqlPredicateAnd.getAndedPredicates()));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateBetween sqlPredicateBetween) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateBetween);
            builder.add(LEFT, sqlPredicateBetween.getBetweenLeft().accept(this));
            builder.add(RIGHT, sqlPredicateBetween.getBetweenRight().accept(this));
            builder.add(EXPRESSION, sqlPredicateBetween.getExpression().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateEqual sqlPredicateEqual) throws AdapterException {
            final JsonObjectBuilder builder = getJsonObjectBuilderForComparison(sqlPredicateEqual);
            return builder.build();
        }

        private JsonObjectBuilder getJsonObjectBuilderForComparison(final AbstractSqlBinaryEquality sqlPredicateEqual)
                throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateEqual);
            builder.add(LEFT, sqlPredicateEqual.getLeft().accept(this));
            builder.add(RIGHT, sqlPredicateEqual.getRight().accept(this));
            return builder;
        }

        @Override
        public JsonObject visit(final SqlPredicateInConstList sqlPredicateInConstList) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateInConstList);
            builder.add(EXPRESSION, sqlPredicateInConstList.getExpression().accept(this));
            builder.add(ARGUMENTS, convertListOfNodes(sqlPredicateInConstList.getInArguments()));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateIsJson sqlPredicateIsJson) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateIsJson);
            builder.add(EXPRESSION, sqlPredicateIsJson.getExpression().accept(this));
            builder.add(TYPE_CONSTRAINT, sqlPredicateIsJson.getTypeConstraint());
            builder.add(KEY_UNIQUENESS_CONSTRAINT, sqlPredicateIsJson.getKeyUniquenessConstraint());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateIsNotJson sqlPredicateIsNotJson) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateIsNotJson);
            builder.add(EXPRESSION, sqlPredicateIsNotJson.getExpression().accept(this));
            builder.add(TYPE_CONSTRAINT, sqlPredicateIsNotJson.getTypeConstraint());
            builder.add(KEY_UNIQUENESS_CONSTRAINT, sqlPredicateIsNotJson.getKeyUniquenessConstraint());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateLess sqlPredicateLess) throws AdapterException {
            final JsonObjectBuilder builder = getJsonObjectBuilderForComparison(sqlPredicateLess);
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateLessEqual sqlPredicateLessEqual) throws AdapterException {
            final JsonObjectBuilder builder = getJsonObjectBuilderForComparison(sqlPredicateLessEqual);
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateLike sqlPredicateLike) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateLike);
            builder.add(EXPRESSION, sqlPredicateLike.getLeft().accept(this));
            addIfPresent(sqlPredicateLike.getPattern(), PATTERN, builder);
            addIfPresent(sqlPredicateLike.getEscapeChar(), ESCAPE_CHAR, builder);
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateLikeRegexp sqlPredicateLikeRegexp) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateLikeRegexp);
            builder.add(EXPRESSION, sqlPredicateLikeRegexp.getLeft().accept(this));
            builder.add(PATTERN, sqlPredicateLikeRegexp.getPattern().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateNot sqlPredicateNot) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateNot);
            builder.add(EXPRESSION, sqlPredicateNot.getExpression().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateNotEqual sqlPredicateNotEqual) throws AdapterException {
            final JsonObjectBuilder builder = getJsonObjectBuilderForComparison(sqlPredicateNotEqual);
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateOr sqlPredicateOr) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateOr);
            builder.add(EXPRESSIONS, convertListOfNodes(sqlPredicateOr.getOrPredicates()));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateIsNotNull sqlPredicateIsNotNull) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateIsNotNull);
            builder.add(EXPRESSION, sqlPredicateIsNotNull.getExpression().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlPredicateIsNull sqlPredicateIsNull) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlPredicateIsNull);
            builder.add(EXPRESSION, sqlPredicateIsNull.getExpression().accept(this));
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlTable sqlTable) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlTable);
            builder.add(NAME, sqlTable.getName());
            if (sqlTable.hasAlias()) {
                builder.add(ALIAS, sqlTable.getAlias());
            }
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlJoin sqlJoin) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(sqlJoin);
            builder.add(LEFT, sqlJoin.getLeft().accept(this));
            builder.add(RIGHT, sqlJoin.getRight().accept(this));
            builder.add(CONDITION, sqlJoin.getCondition().accept(this));
            builder.add(JOIN_TYPE, sqlJoin.getJoinType().name().toLowerCase());
            return builder.build();
        }

        @Override
        public JsonObject visit(final SqlFunctionAggregateListagg function) throws AdapterException {
            final JsonObjectBuilder builder = createObjectBuilderFor(function);
            builder.add(NAME, "LISTAGG");
            builder.add(ARGUMENTS, convertListOfNodes(List.of(function.getArgument())));
            builder.add(OVERFLOW_BEHAVIOUR, render(function.getOverflowBehavior()));
            builder.add(DISTINCT, function.hasDistinct());
            addIfPresent(function.getOrderBy(), ORDER_BY, builder);
            addIfPresent(function.getSeparator(), SEPARATOR, builder);
            return builder.build();
        }

        private JsonObject render(final SqlFunctionAggregateListagg.Behavior overflowBehavior) throws AdapterException {
            final JsonObjectBuilder builder = JSON.createObjectBuilder();
            builder.add(TYPE, overflowBehavior.getBehaviorType().name());
            if (overflowBehavior.getTruncationType() != null) {
                builder.add(TRUNCATION_TYPE, overflowBehavior.getTruncationType());
            }
            if (overflowBehavior.hasTruncationFiller()) {
                builder.add(TRUNCATION_FILLER, overflowBehavior.getTruncationFiller().accept(this));
            }
            return builder.build();
        }
    }
}
