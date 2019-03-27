package com.exasol.adapter.request.parser;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.DataType.IntervalType;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.sql.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class PushdownSqlParser extends AbstractRequestParser {
    private static final String ORDER_BY = "orderBy";
    private static final String EXPRESSION = "expression";
    private static final String RIGHT = "right";
    private static final String VALUE = "value";
    private static final String ARGUMENTS = "arguments";
    private static final String DISTINCT = "distinct";
    private static final String DATA_TYPE = "dataType";

    private final List<TableMetadata> involvedTablesMetadata;

    private PushdownSqlParser(final List<TableMetadata> involvedTablesMetadata) {
        this.involvedTablesMetadata = involvedTablesMetadata;
    }

    public SqlNode parseExpression(final JsonObject expression) {
        final String typeName = expression.getString("type", "");
        final SqlNodeType type = fromTypeName(typeName);
        final SqlNode sqlNode;
        switch (type) {
            case SELECT:
                sqlNode = parseSelect(expression);
                break;
            case TABLE:
                sqlNode = parseTable(expression);
                break;
            case JOIN:
                sqlNode = parseJoin(expression);
                break;
            case COLUMN:
                sqlNode = parseColumn(expression);
                break;
            case LITERAL_NULL:
                sqlNode = parseLiteralNull();
                break;
            case LITERAL_BOOL:
                sqlNode = parseLiteralBool(expression);
                break;
            case LITERAL_DATE:
                sqlNode = parseLiteralDate(expression);
                break;
            case LITERAL_TIMESTAMP:
                sqlNode = parseLiteralTimestamp(expression);
                break;
            case LITERAL_TIMESTAMPUTC:
                sqlNode = parseLiteralTimestamputc(expression);
                break;
            case LITERAL_DOUBLE:
                sqlNode = parseLiteralDouble(expression);
                break;
            case LITERAL_EXACTNUMERIC:
                sqlNode = parseLiteralExactNumeric(expression);
                break;
            case LITERAL_STRING:
                sqlNode = parseLiteralString(expression);
                break;
            case LITERAL_INTERVAL:
                sqlNode = parseLiteralInterval(expression);
                break;
            case PREDICATE_AND:
                sqlNode = parsePredicateAnd(expression);
                break;
            case PREDICATE_OR:
                sqlNode = parsePredicateOr(expression);
                break;
            case PREDICATE_NOT:
                sqlNode = parsePredicateNot(expression);
                break;
            case PREDICATE_EQUAL:
                sqlNode = parsePredicateEqual(expression);
                break;
            case PREDICATE_NOTEQUAL:
                sqlNode = parsePredicateNotEqual(expression);
                break;
            case PREDICATE_LESS:
                sqlNode = parsePredicateLess(expression);
                break;
            case PREDICATE_LESSEQUAL:
                sqlNode = parsePredicateLessEqual(expression);
                break;
            case PREDICATE_LIKE:
                sqlNode = parsePredicateLike(expression);
                break;
            case PREDICATE_LIKE_REGEXP:
                sqlNode = parsePredicateLikeRegexp(expression);
                break;
            case PREDICATE_BETWEEN:
                sqlNode = parsePredicateBetween(expression);
                break;
            case PREDICATE_IN_CONSTLIST:
                sqlNode = parsePredicateInConstlist(expression);
                break;
            case PREDICATE_IS_NULL:
                sqlNode = parsePredicateIsNull(expression);
                break;
            case PREDICATE_IS_NOT_NULL:
                sqlNode = parsePredicateIsNotNull(expression);
                break;
            case FUNCTION_SCALAR:
                sqlNode = parseFunctionScalar(expression);
                break;
            case FUNCTION_SCALAR_EXTRACT:
                sqlNode = parseFunctionScalarExtract(expression);
                break;
            case FUNCTION_SCALAR_CASE:
                sqlNode = parseFunctionScalarCase(expression);
                break;
            case FUNCTION_SCALAR_CAST:
                sqlNode = parseFunctionScalarCast(expression);
                break;
            case FUNCTION_AGGREGATE:
                sqlNode = parseFunctionAggregate(expression);
                break;
            case FUNCTION_AGGREGATE_GROUP_CONCAT:
                sqlNode = parseFunctionAggregateGroupConcat(expression);
                break;
            default:
                throw new IllegalArgumentException("Unknown node type: " + typeName);
        }
        return sqlNode;
    }

    private SqlStatementSelect parseSelect(final JsonObject select) {
        // FROM clause
        final SqlNode table = parseExpression(select.getJsonObject("from"));
        assert (table.getType() == SqlNodeType.TABLE) || (table.getType() == SqlNodeType.JOIN);
        // SELECT list
        final SqlSelectList selectList = parseSelectList(select.getJsonArray("selectList"));
        final SqlExpressionList groupByClause = parseGroupBy(select.getJsonArray("groupBy"));
        // WHERE clause
        SqlNode whereClause = null;
        if (select.containsKey("filter")) {
            whereClause = parseExpression(select.getJsonObject("filter"));
        }
        SqlNode having = null;
        if (select.containsKey("having")) {
            having = parseExpression(select.getJsonObject("having"));
        }
        SqlOrderBy orderBy = null;
        if (select.containsKey(ORDER_BY)) {
            orderBy = parseOrderBy(select.getJsonArray(ORDER_BY));
        }
        SqlLimit limit = null;
        if (select.containsKey("limit")) {
            limit = parseLimit(select.getJsonObject("limit"));
        }
        return new SqlStatementSelect(table, selectList, whereClause, groupByClause, having, orderBy, limit);
    }

    private SqlNode parseTable(final JsonObject exp) {
        final String tableName = exp.getString("name");
        final TableMetadata tableMetadata = findInvolvedTableMetadata(tableName);
        if (exp.containsKey("alias")) {
            final String tableAlias = exp.getString("alias");
            return new SqlTable(tableName, tableAlias, tableMetadata);
        } else {
            return new SqlTable(tableName, tableMetadata);
        }
    }

    private SqlNode parseJoin(final JsonObject exp) {
        final SqlNode left = parseExpression(exp.getJsonObject("left"));
        final SqlNode right = parseExpression(exp.getJsonObject(RIGHT));
        final SqlNode condition = parseExpression(exp.getJsonObject("condition"));
        final JoinType joinType = fromJoinTypeName(exp.getString("join_type"));
        return new SqlJoin(left, right, condition, joinType);
    }

    private SqlNode parseColumn(final JsonObject exp) {
        final int columnId = exp.getInt("columnNr");
        final String columnName = exp.getString("name");
        final String tableName = exp.getString("tableName");
        final ColumnMetadata columnMetadata = findColumnMetadata(tableName, columnName);
        if (exp.containsKey("tableAlias")) {
            final String tableAlias = exp.getString("tableAlias");
            return new SqlColumn(columnId, columnMetadata, tableName, tableAlias);
        } else {
            return new SqlColumn(columnId, columnMetadata, tableName);
        }
    }

    private SqlNode parseLiteralNull() {
        return new SqlLiteralNull();
    }

    private SqlNode parseLiteralBool(final JsonObject expression) {
        final boolean boolVal = expression.getBoolean(VALUE);
        return new SqlLiteralBool(boolVal);
    }

    private List<SqlNode> parseExpressionList(final JsonArray array) {
        assert array != null;
        final List<SqlNode> sqlNodes = new ArrayList<>();
        for (final JsonObject expr : array.getValuesAs(JsonObject.class)) {
            final SqlNode node = parseExpression(expr);
            sqlNodes.add(node);
        }
        return sqlNodes;
    }

    private SqlGroupBy parseGroupBy(final JsonArray groupBy) {
        if (groupBy == null) {
            return null;
        }
        final List<SqlNode> groupByElements = parseExpressionList(groupBy);
        return new SqlGroupBy(groupByElements);
    }

    private SqlNode parseLiteralDate(final JsonObject exp) {
        final String date = exp.getString(VALUE);
        return new SqlLiteralDate(date);
    }

    private SqlSelectList parseSelectList(final JsonArray selectList) {
        if (selectList == null) {
            // this is like SELECT *
            return SqlSelectList.createSelectStarSelectList();
        }
        final List<SqlNode> selectListElements = parseExpressionList(selectList);
        if (selectListElements.isEmpty()) {
            return SqlSelectList.createAnyValueSelectList();
        } else {
            return SqlSelectList.createRegularSelectList(selectListElements);
        }
    }

    private SqlOrderBy parseOrderBy(final JsonArray orderByList) {
        final List<SqlNode> orderByExpressions = new ArrayList<>();
        final List<Boolean> isAsc = new ArrayList<>();
        final List<Boolean> nullsLast = new ArrayList<>();
        for (int i = 0; i < orderByList.size(); ++i) {
            final JsonObject orderElem = orderByList.getJsonObject(i);
            orderByExpressions.add(parseExpression(orderElem.getJsonObject(EXPRESSION)));
            isAsc.add(orderElem.getBoolean("isAscending", true));
            nullsLast.add(orderElem.getBoolean("nullsLast", true));
        }
        return new SqlOrderBy(orderByExpressions, isAsc, nullsLast);
    }

    private SqlLimit parseLimit(final JsonObject limit) {
        final int numElements = limit.getInt("numElements");
        final int offset = limit.getInt("offset", 0);
        return new SqlLimit(numElements, offset);
    }

    private SqlNode parseLiteralTimestamp(final JsonObject exp) {
        final String timestamp = exp.getString(VALUE);
        return new SqlLiteralTimestamp(timestamp);
    }

    private SqlNode parsePredicateIsNotNull(final JsonObject exp) {
        final SqlNode isNotnullExp = parseExpression(exp.getJsonObject(EXPRESSION));
        return new SqlPredicateIsNotNull(isNotnullExp);
    }

    private SqlNode parsePredicateIsNull(final JsonObject exp) {
        final SqlNode isnullExp = parseExpression(exp.getJsonObject(EXPRESSION));
        return new SqlPredicateIsNull(isnullExp);
    }

    private SqlNode parsePredicateLike(final JsonObject exp) {
        final SqlNode likeLeft = parseExpression(exp.getJsonObject(EXPRESSION));
        final SqlNode likePattern = parseExpression(exp.getJsonObject("pattern"));
        if (exp.containsKey("escapeChar")) {
            final SqlNode escapeChar = parseExpression(exp.getJsonObject("escapeChar"));
            return new SqlPredicateLike(likeLeft, likePattern, escapeChar);
        }
        return new SqlPredicateLike(likeLeft, likePattern);
    }

    private SqlNode parsePredicateLessEqual(final JsonObject exp) {
        final SqlNode lessEqLeft = parseExpression(exp.getJsonObject("left"));
        final SqlNode lessEqRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateLessEqual(lessEqLeft, lessEqRight);
    }

    private SqlNode parsePredicateLess(final JsonObject exp) {
        final SqlNode lessLeft = parseExpression(exp.getJsonObject("left"));
        final SqlNode lessRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateLess(lessLeft, lessRight);
    }

    private SqlNode parsePredicateNotEqual(final JsonObject exp) {
        final SqlNode notEqualLeft = parseExpression(exp.getJsonObject("left"));
        final SqlNode notEqualRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateNotEqual(notEqualLeft, notEqualRight);
    }

    private SqlNode parsePredicateEqual(final JsonObject exp) {
        final SqlNode equalLeft = parseExpression(exp.getJsonObject("left"));
        final SqlNode equalRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateEqual(equalLeft, equalRight);
    }

    private SqlNode parsePredicateNot(final JsonObject exp) {
        final SqlNode notExp = parseExpression(exp.getJsonObject(EXPRESSION));
        return new SqlPredicateNot(notExp);
    }

    private SqlNode parsePredicateOr(final JsonObject exp) {
        final List<SqlNode> orPredicates = new ArrayList<>();
        for (final JsonObject pred : exp.getJsonArray("expressions").getValuesAs(JsonObject.class)) {
            orPredicates.add(parseExpression(pred));
        }
        return new SqlPredicateOr(orPredicates);
    }

    private SqlNode parsePredicateAnd(final JsonObject exp) {
        final List<SqlNode> andedPredicates = new ArrayList<>();
        for (final JsonObject pred : exp.getJsonArray("expressions").getValuesAs(JsonObject.class)) {
            andedPredicates.add(parseExpression(pred));
        }
        return new SqlPredicateAnd(andedPredicates);
    }

    private SqlNode parseLiteralInterval(final JsonObject expression) {
        final String intervalVal = expression.getString(VALUE);
        final DataType intervalType = getDataType(expression.getJsonObject(DATA_TYPE));
        return new SqlLiteralInterval(intervalVal, intervalType);
    }

    private DataType getDataType(final JsonObject dataType) {
        final String typeName = dataType.getString("type").toUpperCase();
        final DataType type;
        switch (typeName) {
            case "DECIMAL":
                type = DataType.createDecimal(dataType.getInt("precision"), dataType.getInt("scale"));
                break;
            case "DOUBLE":
                type = DataType.createDouble();
                break;
            case "VARCHAR":
                type = getVarchar(dataType);
                break;
            case "CHAR":
                type = getChar(dataType);
                break;
            case "BOOLEAN":
                type = DataType.createBool();
                break;
            case "DATE":
                type = DataType.createDate();
                break;
            case "TIMESTAMP":
                type = getTimestamp(dataType);
                break;
            case "INTERVAL":
                type = getInterval(dataType);
                break;
            case "GEOMETRY":
                type = getGeometry(dataType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported data type encountered: " + typeName);
        }
        return type;
    }

    private DataType getVarchar(final JsonObject dataType) {
        final DataType type;
        final String charSet = dataType.getString("characterSet", "UTF8");
        type = DataType.createVarChar(dataType.getInt("size"), charSetFromString(charSet));
        return type;
    }

    private DataType getChar(final JsonObject dataType) {
        final DataType type;
        final String charSet = dataType.getString("characterSet", "UTF8");
        type = DataType.createChar(dataType.getInt("size"), charSetFromString(charSet));
        return type;
    }

    private DataType getTimestamp(final JsonObject dataType) {
        final DataType type;
        final boolean withLocalTimezone = dataType.getBoolean("withLocalTimeZone", false);
        type = DataType.createTimestamp(withLocalTimezone);
        return type;
    }

    private DataType getInterval(final JsonObject dataType) {
        final DataType type;
        final int precision = dataType.getInt("precision", 2);
        final IntervalType intervalType = intervalTypeFromString(dataType.getString("fromTo"));
        if (intervalType == IntervalType.DAY_TO_SECOND) {
            final int fraction = dataType.getInt("fraction", 3);
            type = DataType.createIntervalDaySecond(precision, fraction);
        } else {
            assert intervalType == IntervalType.YEAR_TO_MONTH;
            type = DataType.createIntervalYearMonth(precision);
        }
        return type;
    }

    private DataType getGeometry(final JsonObject dataType) {
        final DataType type;
        final int srid = dataType.getInt("srid");
        type = DataType.createGeometry(srid);
        return type;
    }

    private static ExaCharset charSetFromString(final String charset) {
        if (charset.equals("UTF8")) {
            return ExaCharset.UTF8;
        } else if (charset.equals("ASCII")) {
            return ExaCharset.ASCII;
        } else {
            throw new IllegalArgumentException(
                  "Unsupported charset encountered: " + charset + ". Supported charsets are \"UTF8\" and \"ASCII\".");
        }
    }

    private static IntervalType intervalTypeFromString(final String intervalType) {
        if (intervalType.equals("DAY TO SECONDS")) {
            return IntervalType.DAY_TO_SECOND;
        } else if (intervalType.equals("YEAR TO MONTH")) {
            return IntervalType.YEAR_TO_MONTH;
        } else {
            throw new IllegalArgumentException("Unsupported interval data type encountered: " + intervalType //
                  + " Supported intervals are \"DAY TO SECONDS\" and \"YEAR TO MONTH\".");
        }
    }

    private SqlNode parseLiteralString(final JsonObject exp) {
        final String stringVal = exp.getString(VALUE);
        return new SqlLiteralString(stringVal);
    }

    private SqlNode parseLiteralExactNumeric(final JsonObject exp) {
        final BigDecimal exactVal = new BigDecimal(exp.getString(VALUE));
        return new SqlLiteralExactnumeric(exactVal);
    }

    private SqlNode parseLiteralDouble(final JsonObject exp) {
        final String doubleString = exp.getString(VALUE);
        return new SqlLiteralDouble(Double.parseDouble(doubleString));
    }

    private SqlNode parseLiteralTimestamputc(final JsonObject exp) {
        final String timestampUtc = exp.getString(VALUE);
        return new SqlLiteralTimestampUtc(timestampUtc);
    }

    private SqlNode parsePredicateLikeRegexp(final JsonObject exp) {
        final SqlNode likeRegexpLeft = parseExpression(exp.getJsonObject(EXPRESSION));
        final SqlNode likeRegexpPattern = parseExpression(exp.getJsonObject("pattern"));
        return new SqlPredicateLikeRegexp(likeRegexpLeft, likeRegexpPattern);
    }

    private SqlNode parsePredicateBetween(final JsonObject exp) {
        final SqlNode betweenExp = parseExpression(exp.getJsonObject(EXPRESSION));
        final SqlNode betweenLeft = parseExpression(exp.getJsonObject("left"));
        final SqlNode betweenRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateBetween(betweenExp, betweenLeft, betweenRight);
    }

    private SqlNode parsePredicateInConstlist(final JsonObject exp) {
        final SqlNode inExp = parseExpression(exp.getJsonObject(EXPRESSION));
        final List<SqlNode> inArguments = new ArrayList<>();
        for (final JsonObject pred : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
            inArguments.add(parseExpression(pred));
        }
        return new SqlPredicateInConstList(inExp, inArguments);
    }

    private SqlNode parseFunctionScalar(final JsonObject exp) {
        final String functionName = exp.getString("name");
        boolean hasVariableInputArgs = false;
        final int numArgs;
        if (exp.containsKey("variableInputArgs")) {
            hasVariableInputArgs = exp.getBoolean("variableInputArgs");
        }
        final List<SqlNode> arguments = new ArrayList<>();
        for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
            arguments.add(parseExpression(argument));
        }
        if (!hasVariableInputArgs) {
            numArgs = exp.getInt("numArgs");
            assert numArgs == arguments.size();
        }
        boolean isInfix = false;
        if (exp.containsKey("infix")) {
            isInfix = exp.getBoolean("infix");
        }
        boolean isPrefix = false;
        if (exp.containsKey("prefix")) {
            isPrefix = exp.getBoolean("prefix");
        }
        return new SqlFunctionScalar(fromScalarFunctionName(functionName), arguments, isInfix, isPrefix);
    }

    private SqlNode parseFunctionScalarExtract(final JsonObject exp) {
        final String toExtract = exp.getString("toExtract");
        final List<SqlNode> extractArguments = new ArrayList<>();
        if (exp.containsKey(ARGUMENTS)) {
            for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
                extractArguments.add(parseExpression(argument));
            }
        }
        return new SqlFunctionScalarExtract(toExtract, extractArguments);
    }

    private SqlNode parseFunctionScalarCase(final JsonObject exp) {
        final List<SqlNode> caseArguments = new ArrayList<>();
        final List<SqlNode> caseResults = new ArrayList<>();
        SqlNode caseBasis = null;
        if (exp.containsKey(ARGUMENTS)) {
            for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
                caseArguments.add(parseExpression(argument));
            }
        }
        if (exp.containsKey("results")) {
            for (final JsonObject argument : exp.getJsonArray("results").getValuesAs(JsonObject.class)) {
                caseResults.add(parseExpression(argument));
            }
        }
        if (exp.containsKey("basis")) {
            caseBasis = parseExpression(exp.getJsonObject("basis"));
        }
        return new SqlFunctionScalarCase(caseArguments, caseResults, caseBasis);
    }

    private SqlNode parseFunctionScalarCast(final JsonObject exp) {
        final DataType castDataType = getDataType(exp.getJsonObject(DATA_TYPE));
        final List<SqlNode> castArguments = new ArrayList<>();
        if (exp.containsKey(ARGUMENTS)) {
            for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
                castArguments.add(parseExpression(argument));
            }
        }
        return new SqlFunctionScalarCast(castDataType, castArguments);
    }

    private SqlNode parseFunctionAggregate(final JsonObject exp) {
        final String setFunctionName = exp.getString("name");
        final List<SqlNode> setArguments = new ArrayList<>();
        boolean distinct = false;
        if (exp.containsKey(DISTINCT)) {
            distinct = exp.getBoolean(DISTINCT);
        }
        if (exp.containsKey(ARGUMENTS)) {
            for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
                setArguments.add(parseExpression(argument));
            }
        }
        return new SqlFunctionAggregate(fromAggregationFunctionName(setFunctionName), setArguments, distinct);
    }

    private SqlNode parseFunctionAggregateGroupConcat(final JsonObject exp) {
        final String functionName = exp.getString("name");
        final List<SqlNode> setArguments = new ArrayList<>();
        boolean distinct = false;
        if (exp.containsKey(DISTINCT)) {
            distinct = exp.getBoolean(DISTINCT);
        }
        if (exp.containsKey(ARGUMENTS)) {
            for (final JsonObject argument : exp.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class)) {
                setArguments.add(parseExpression(argument));
            }
        }
        SqlOrderBy orderBy = null;
        if (exp.containsKey(ORDER_BY)) {
            orderBy = parseOrderBy(exp.getJsonArray(ORDER_BY));
        }
        String separator = null;
        if (exp.containsKey("separator")) {
            separator = exp.getString("separator");
        }
        return new SqlFunctionAggregateGroupConcat(fromAggregationFunctionName(functionName), setArguments, orderBy,
              distinct, separator);
    }

    /**
     * Mapping from join type name (as in json api) to enum
     */
    private static JoinType fromJoinTypeName(final String typeName) {
        return Enum.valueOf(JoinType.class, typeName.toUpperCase());
    }

    /**
     * Mapping from scalar function name (as in json api) to enum
     */
    private static ScalarFunction fromScalarFunctionName(final String functionName) {
        return Enum.valueOf(ScalarFunction.class, functionName.toUpperCase());
    }

    /**
     * Mapping from aggregate function name (as in json api) to enum
     */
    private static AggregateFunction fromAggregationFunctionName(final String functionName) {
        return Enum.valueOf(AggregateFunction.class, functionName.toUpperCase());
    }

    /**
     * Mapping from type name (as in json api) to enum
     */
    private static SqlNodeType fromTypeName(final String typeName) {
        return Enum.valueOf(SqlNodeType.class, typeName.toUpperCase());
    }

    private TableMetadata findInvolvedTableMetadata(final String tableName) {
        assert this.involvedTablesMetadata != null;
        for (final TableMetadata tableMetadata : this.involvedTablesMetadata) {
            if (tableMetadata.getName().equals(tableName)) {
                return tableMetadata;
            }
        }
        throw new IllegalStateException(
              "Could not find table metadata for involved table " + tableName + ". All involved tables: " +
                    this.involvedTablesMetadata.toString());
    }

    private ColumnMetadata findColumnMetadata(final String tableName, final String columnName) {
        final TableMetadata tableMetadata = findInvolvedTableMetadata(tableName);
        for (final ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
            if (columnMetadata.getName().equals(columnName)) {
                return columnMetadata;
            }
        }
        throw new IllegalStateException(
              "Could not find column metadata for involved table " + tableName + " and column + " + columnName +
                    ". All involved tables: " + this.involvedTablesMetadata.toString());
    }

    /**
     * Create an instance of a {@link PushdownSqlParser}
     *
     * @param involvedTableMetadata metadata for all tables that are referred to in the push-down request
     * @return new instance
     */
    public static PushdownSqlParser createWithTablesMetadata(final List<TableMetadata> involvedTableMetadata) {
        return new PushdownSqlParser(involvedTableMetadata);
    }
}