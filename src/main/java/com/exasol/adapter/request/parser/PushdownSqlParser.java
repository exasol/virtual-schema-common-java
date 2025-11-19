package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.RequestJsonKeys.*;

import java.math.BigDecimal;
import java.util.*;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.DataType.IntervalType;
import com.exasol.adapter.metadata.converter.SchemaMetadataJsonConverter;
import com.exasol.adapter.sql.*;
import com.exasol.adapter.sql.AbstractSqlPredicateJson.KeyUniquenessConstraint;
import com.exasol.adapter.sql.AbstractSqlPredicateJson.TypeConstraints;
import com.exasol.adapter.sql.SqlFunctionAggregateListagg.*;
import com.exasol.adapter.sql.SqlFunctionAggregateListagg.Behavior.TruncationType;
import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

/**
 * Parser for the JSON query AST.
 */
public final class PushdownSqlParser extends AbstractRequestParser {
    private final List<TableMetadata> involvedTablesMetadata;

    private PushdownSqlParser(final List<TableMetadata> involvedTablesMetadata) {
        this.involvedTablesMetadata = involvedTablesMetadata;
    }

    private static ExaCharset charSetFromString(final String charset) {
        if (charset.equals("UTF8")) {
            return ExaCharset.UTF8;
        } else if (charset.equals("ASCII")) {
            return ExaCharset.ASCII;
        } else {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-12")
                    .message("Unsupported charset encountered: {{charset}}. Supported charsets are 'UTF8' and 'ASCII'.")
                    .parameter("charset", charset).toString());
        }
    }

    private SqlStatementSelect parseSelect(final JsonObject select) {
        // FROM clause
        final SqlNode from = parseExpression(select.getJsonObject("from"));
        // SELECT list
        final SqlSelectList selectList = createSelectList(select, from);
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
        final SqlExpressionList groupByClause = parseGroupBy(select, selectList, having);
        return SqlStatementSelect.builder().selectList(selectList).fromClause(from).whereClause(whereClause)
                .groupBy(groupByClause).having(having).orderBy(orderBy).limit(limit).build();
    }

    private SqlSelectList createSelectList(final JsonObject select, final SqlNode from) {
        final JsonArray selectListJson = select.getJsonArray("selectList");
        if (selectListJson == null) {
            return SqlSelectList.createRegularSelectList(collectAllInvolvedColumns(from));
        } else {
            return parseSelectList(selectListJson);
        }
    }

    private SqlNode parseTable(final JsonObject exp) {
        final String tableName = exp.getString(NAME);
        final TableMetadata tableMetadata = findInvolvedTableMetadata(tableName);
        if (exp.containsKey(ALIAS)) {
            final String tableAlias = exp.getString(ALIAS);
            return new SqlTable(tableName, tableAlias, tableMetadata);
        } else {
            return new SqlTable(tableName, tableMetadata);
        }
    }

    private SqlNode parseJoin(final JsonObject exp) {
        final SqlNode left = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode right = parseExpression(exp.getJsonObject(RIGHT));
        final SqlNode condition = parseExpression(exp.getJsonObject(CONDITION));
        final JoinType joinType = fromJoinTypeName(exp.getString(JOIN_TYPE));
        return new SqlJoin(left, right, condition, joinType);
    }

    private SqlNode parseColumn(final JsonObject exp) {
        final int columnId = exp.getInt(COLUMN_NR);
        final String columnName = exp.getString(NAME);
        final String tableName = exp.getString(TABLE_NAME);
        final ColumnMetadata columnMetadata = findColumnMetadata(tableName, columnName);
        if (exp.containsKey(TABLE_ALIAS)) {
            final String tableAlias = exp.getString(TABLE_ALIAS);
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
        final List<SqlNode> sqlNodes = new ArrayList<>();
        for (final JsonObject expr : array.getValuesAs(JsonObject.class)) {
            sqlNodes.add(parseExpression(expr));
        }
        return sqlNodes;
    }

    private SqlGroupBy parseGroupBy(
            final JsonObject select,
            final SqlSelectList selectList,
            final SqlNode having) {
        if (hasSingleGroupAggregation(select) &&
                !hasAggregateFunction(selectList.getExpressions()) &&
                !hasAggregateFunction(List.of(having))) {
            // If the aggregationType is single_group and there is no an aggregate function,
            // we limit the result to a single row.
            return new SqlGroupBy(List.of(new SqlLiteralString("a")), true);
        } else {
            final JsonArray groupBy = select.getJsonArray("groupBy");
            return groupBy == null ? null : new SqlGroupBy(parseExpressionList(groupBy), false);
        }
    }

    private boolean hasAggregateFunction(final List<SqlNode> nodesList) {
        ArrayDeque<SqlNode> expressions = new ArrayDeque<>(nodesList);
        while (!expressions.isEmpty()) {
            final SqlNode expression = expressions.poll();
            if (expression != null) {
                expressions.addAll(expression.getChildren());
                if (expression.getType().equals(SqlNodeType.FUNCTION_AGGREGATE)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasSingleGroupAggregation(final JsonObject select) {
        return select.getString(AGGREGATION_TYPE, null) != null
                && select.getString(AGGREGATION_TYPE).equals(AGGREGATION_TYPE_SINGLE_GROUP);
    }

    private SqlNode parseLiteralDate(final JsonObject exp) {
        final String date = exp.getString(VALUE);
        return new SqlLiteralDate(date);
    }

    private SqlSelectList parseSelectList(final JsonArray selectList) {
        final List<SqlNode> selectListElements = parseExpressionList(selectList);
        if (selectListElements.isEmpty()) {
            return SqlSelectList.createAnyValueSelectList();
        } else {
            return SqlSelectList.createRegularSelectList(selectListElements);
        }
    }

    private static IntervalType intervalTypeFromString(final String intervalType) {
        if (intervalType.equals("DAY TO SECONDS")) {
            return IntervalType.DAY_TO_SECOND;
        } else if (intervalType.equals("YEAR TO MONTH")) {
            return IntervalType.YEAR_TO_MONTH;
        } else {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-13").message(
                    "Unsupported interval data type encountered: {{intervalType}}. Supported intervals are 'DAY TO SECONDS' and 'YEAR TO MONTH'.")
                    .parameter("intervalType", intervalType).toString());
        }
    }

    /**
     * Parse an expression.
     * 
     * @param expression JSON object of the SQL expression
     * @return parsed expression
     */
    public SqlNode parseExpression(final JsonObject expression) {
        final String typeName = expression.getString(TYPE, "");
        final SqlNodeType type = fromTypeName(typeName);
        switch (type) {
        case SELECT:
            return parseSelect(expression);
        case TABLE:
            return parseTable(expression);
        case JOIN:
            return parseJoin(expression);
        case COLUMN:
            return parseColumn(expression);
        case LITERAL_NULL:
            return parseLiteralNull();
        case LITERAL_BOOL:
            return parseLiteralBool(expression);
        case LITERAL_DATE:
            return parseLiteralDate(expression);
        case LITERAL_TIMESTAMP:
            return parseLiteralTimestamp(expression);
        case LITERAL_TIMESTAMPUTC:
            return parseLiteralTimestamputc(expression);
        case LITERAL_DOUBLE:
            return parseLiteralDouble(expression);
        case LITERAL_EXACTNUMERIC:
            return parseLiteralExactNumeric(expression);
        case LITERAL_STRING:
            return parseLiteralString(expression);
        case LITERAL_INTERVAL:
            return parseLiteralInterval(expression);
        case PREDICATE_AND:
            return parsePredicateAnd(expression);
        case PREDICATE_OR:
            return parsePredicateOr(expression);
        case PREDICATE_NOT:
            return parsePredicateNot(expression);
        case PREDICATE_EQUAL:
            return parsePredicateEqual(expression);
        case PREDICATE_NOTEQUAL:
            return parsePredicateNotEqual(expression);
        case PREDICATE_LESS:
            return parsePredicateLess(expression);
        case PREDICATE_LESSEQUAL:
            return parsePredicateLessEqual(expression);
        case PREDICATE_LIKE:
            return parsePredicateLike(expression);
        case PREDICATE_LIKE_REGEXP:
            return parsePredicateLikeRegexp(expression);
        case PREDICATE_BETWEEN:
            return parsePredicateBetween(expression);
        case PREDICATE_IN_CONSTLIST:
            return parsePredicateInConstlist(expression);
        case PREDICATE_IS_JSON:
            return parsePredicateIsJson(expression);
        case PREDICATE_IS_NOT_JSON:
            return parsePredicateIsNotJson(expression);
        case PREDICATE_IS_NULL:
            return parsePredicateIsNull(expression);
        case PREDICATE_IS_NOT_NULL:
            return parsePredicateIsNotNull(expression);
        case FUNCTION_SCALAR:
            return parseFunctionScalar(expression);
        case FUNCTION_SCALAR_EXTRACT:
            return parseFunctionScalarExtract(expression);
        case FUNCTION_SCALAR_CASE:
            return parseFunctionScalarCase(expression);
        case FUNCTION_SCALAR_CAST:
            return parseFunctionScalarCast(expression);
        case FUNCTION_SCALAR_JSON_VALUE:
            return parseFunctionScalarJsonValue(expression);
        case FUNCTION_AGGREGATE:
            return parseFunctionAggregate(expression);
        case FUNCTION_AGGREGATE_GROUP_CONCAT:
            return parseFunctionAggregateGroupConcat(expression);
        case FUNCTION_AGGREGATE_LISTAGG:
            return parseFunctionAggregateListagg(expression);
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-8") //
                    .message("Unknown node type: {{typeName}}") //
                    .parameter("typeName", typeName).toString());
        }
    }

    private Map<String, TableMetadata> getInvolvedTablesMetadataMap() {
        final Map<String, TableMetadata> tableMetadataMap = new HashMap<>(this.involvedTablesMetadata.size());
        for (final TableMetadata involvedTableMeta : this.involvedTablesMetadata) {
            tableMetadataMap.put(involvedTableMeta.getName(), involvedTableMeta);
        }
        return tableMetadataMap;
    }

    private SqlColumn createColumn(final int index, final SqlTable table, final ColumnMetadata columnMetadata) {
        if (table.hasAlias()) {
            return new SqlColumn(index, columnMetadata, table.getName(), table.getAlias());
        } else {
            return new SqlColumn(index, columnMetadata, table.getName());
        }
    }

    private SqlOrderBy parseOrderBy(final JsonArray orderByList) {
        final List<SqlNode> orderByExpressions = new ArrayList<>();
        final List<Boolean> isAsc = new ArrayList<>();
        final List<Boolean> nullsLast = new ArrayList<>();
        for (int i = 0; i < orderByList.size(); ++i) {
            final JsonObject orderElem = orderByList.getJsonObject(i);
            orderByExpressions.add(parseExpression(orderElem.getJsonObject(EXPRESSION)));
            isAsc.add(orderElem.getBoolean(IS_ASCENDING, true));
            nullsLast.add(orderElem.getBoolean(NULLS_LAST, true));
        }
        return new SqlOrderBy(orderByExpressions, isAsc, nullsLast);
    }

    private SqlLimit parseLimit(final JsonObject limit) {
        final int numElements = limit.getInt(NUM_ELEMENTS);
        final int offset = limit.getInt(OFFSET, 0);
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
        final SqlNode likePattern = parseExpression(exp.getJsonObject(PATTERN));
        if (exp.containsKey(ESCAPE_CHAR)) {
            final SqlNode escapeChar = parseExpression(exp.getJsonObject(ESCAPE_CHAR));
            return new SqlPredicateLike(likeLeft, likePattern, escapeChar);
        }
        return new SqlPredicateLike(likeLeft, likePattern);
    }

    private SqlNode parsePredicateLessEqual(final JsonObject exp) {
        final SqlNode lessEqLeft = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode lessEqRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateLessEqual(lessEqLeft, lessEqRight);
    }

    private SqlNode parsePredicateLess(final JsonObject exp) {
        final SqlNode lessLeft = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode lessRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateLess(lessLeft, lessRight);
    }

    private SqlNode parsePredicateNotEqual(final JsonObject exp) {
        final SqlNode notEqualLeft = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode notEqualRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateNotEqual(notEqualLeft, notEqualRight);
    }

    private SqlNode parsePredicateEqual(final JsonObject exp) {
        final SqlNode equalLeft = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode equalRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateEqual(equalLeft, equalRight);
    }

    private SqlNode parsePredicateNot(final JsonObject exp) {
        final SqlNode notExp = parseExpression(exp.getJsonObject(EXPRESSION));
        return new SqlPredicateNot(notExp);
    }

    private SqlNode parsePredicateOr(final JsonObject exp) {
        final List<SqlNode> orPredicates = getListOfSqlNodes(exp, EXPRESSIONS);
        return new SqlPredicateOr(orPredicates);
    }

    private List<SqlNode> getListOfSqlNodes(final JsonObject jsonExpression, final String key) {
        final List<SqlNode> arguments = new ArrayList<>();
        if (jsonExpression.containsKey(key)) {
            for (final JsonObject jsonObject : jsonExpression.getJsonArray(key).getValuesAs(JsonObject.class)) {
                arguments.add(parseExpression(jsonObject));
            }
        }
        return arguments;
    }

    private SqlNode parsePredicateAnd(final JsonObject exp) {
        final List<SqlNode> andedPredicates = getListOfSqlNodes(exp, EXPRESSIONS);
        return new SqlPredicateAnd(andedPredicates);
    }

    private SqlNode parseLiteralInterval(final JsonObject expression) {
        final String intervalVal = expression.getString(VALUE);
        final DataType intervalType = getDataType(expression.getJsonObject(DATA_TYPE));
        return new SqlLiteralInterval(intervalVal, intervalType);
    }

    @SuppressWarnings("java:S1192") // tableName is duplicated but that's ok since it's a parameter
    private List<SqlNode> collectAllInvolvedColumns(final SqlNode from) {
        final List<SqlTable> involvedTables = collectInvolvedTables(from);
        final Map<String, TableMetadata> tableMetadataMap = getInvolvedTablesMetadataMap();
        final List<SqlNode> selectListElements = new ArrayList<>();
        for (final SqlTable table : involvedTables) {
            final String tableName = table.getName();
            if (tableMetadataMap.containsKey(tableName)) {
                final List<ColumnMetadata> columns = tableMetadataMap.get(tableName).getColumns();
                for (int i = 0, columnsSize = columns.size(); i < columnsSize; ++i) {
                    selectListElements.add(createColumn(i, table, columns.get(i)));
                }
            } else {
                throw new IllegalStateException(ExaError.messageBuilder("E-VSCOMJAVA-9").message(
                        "Unable to find metadata for table \"{{tableName|uq}}\" during collecting involved columns.")
                        .parameter("tableName", tableName).toString());
            }
        }
        return selectListElements;
    }

    private DataType getHashtype(final JsonObject dataType) {
        final int byteSize = dataType.getInt(BYTE_SIZE);
        return DataType.createHashtype(byteSize);
    }

    private DataType getVarchar(final JsonObject dataType) {
        final String charSet = dataType.getString(CHARACTER_SET, "UTF8");
        return DataType.createVarChar(dataType.getInt(SIZE), charSetFromString(charSet));
    }

    private DataType getChar(final JsonObject dataType) {
        final String charSet = dataType.getString(CHARACTER_SET, "UTF8");
        return DataType.createChar(dataType.getInt(SIZE), charSetFromString(charSet));
    }

    private DataType getTimestamp(final JsonObject dataType) {
        final boolean withLocalTimezone = dataType.getBoolean(WITH_LOCAL_TIME_ZONE, false);
        final int precision = dataType.getInt(SchemaMetadataJsonConverter.TIMESTAMP_PRECISION_KEY,
                DataTypeParser.DEFAULT_TIMESTAMP_PRECISION);
        return DataType.createTimestamp(withLocalTimezone, precision);
    }

    private DataType getInterval(final JsonObject dataType) {
        final int precision = dataType.getInt(PRECISION, 2);
        final IntervalType intervalType = intervalTypeFromString(dataType.getString(FROM_TO));
        if (intervalType == IntervalType.DAY_TO_SECOND) {
            final int fraction = dataType.getInt(FRACTION, 3);
            return DataType.createIntervalDaySecond(precision, fraction);
        } else {
            return DataType.createIntervalYearMonth(precision);
        }
    }

    private DataType getGeometry(final JsonObject dataType) {
        final int srid = dataType.getInt(SRID);
        return DataType.createGeometry(srid);
    }

    /**
     * We collect all tables from the FROM clause in a given order. We need the tables, because the
     * involvedTablesMetadata field doesn't contain `tableAlias` information.
     */
    private List<SqlTable> collectInvolvedTables(final SqlNode from) {
        final List<SqlTable> involvedTables = new ArrayList<>();
        final Stack<SqlNode> nodes = new Stack<>();
        nodes.add(from);
        while (!nodes.isEmpty()) {
            final SqlNode node = nodes.pop();
            switch (node.getType()) {
            case TABLE:
                involvedTables.add((SqlTable) node);
                break;
            case JOIN:
                nodes.add(((SqlJoin) node).getRight());
                nodes.add(((SqlJoin) node).getLeft());
                break;
            default:
                throw new IllegalStateException(ExaError.messageBuilder("E-VSCOMJAVA-10")
                        .message("Encountered illegal SqlNodeType during collection involved tables: {{nodeType}}")
                        .parameter("nodeType", node.getType()).toString());
            }
        }
        return involvedTables;
    }

    private DataType getDataType(final JsonObject dataType) {
        final String typeName = dataType.getString(TYPE).toUpperCase();
        switch (typeName) {
        case "DECIMAL":
            return DataType.createDecimal(dataType.getInt(PRECISION), dataType.getInt(SCALE));
        case "DOUBLE":
            return DataType.createDouble();
        case "VARCHAR":
            return getVarchar(dataType);
        case "CHAR":
            return getChar(dataType);
        case "BOOLEAN":
            return DataType.createBool();
        case "DATE":
            return DataType.createDate();
        case "TIMESTAMP":
            return getTimestamp(dataType);
        case "INTERVAL":
            return getInterval(dataType);
        case "GEOMETRY":
            return getGeometry(dataType);
        case "HASHTYPE":
            return getHashtype(dataType);
        default:
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-11")
                    .message("Unsupported data type encountered: {{typeName}}.") //
                    .parameter("typeName", typeName).toString());
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
        final SqlNode likeRegexpPattern = parseExpression(exp.getJsonObject(PATTERN));
        return new SqlPredicateLikeRegexp(likeRegexpLeft, likeRegexpPattern);
    }

    private SqlNode parsePredicateBetween(final JsonObject exp) {
        final SqlNode betweenExp = parseExpression(exp.getJsonObject(EXPRESSION));
        final SqlNode betweenLeft = parseExpression(exp.getJsonObject(LEFT));
        final SqlNode betweenRight = parseExpression(exp.getJsonObject(RIGHT));
        return new SqlPredicateBetween(betweenExp, betweenLeft, betweenRight);
    }

    private SqlNode parsePredicateIsJson(final JsonObject jsonExpression) {
        final SqlNode expression = parseExpression(jsonExpression.getJsonObject(EXPRESSION));
        final TypeConstraints typeConstraint = TypeConstraints
                .valueOf(jsonExpression.getString(TYPE_CONSTRAINT).toUpperCase());
        final KeyUniquenessConstraint keyUniquenessConstraint = KeyUniquenessConstraint
                .of(jsonExpression.getString(KEY_UNIQUENESS_CONSTRAINT));
        return new SqlPredicateIsJson(expression, typeConstraint, keyUniquenessConstraint);
    }

    private SqlNode parsePredicateIsNotJson(final JsonObject jsonExpression) {
        final SqlNode expression = parseExpression(jsonExpression.getJsonObject(EXPRESSION));
        final TypeConstraints typeConstraint = TypeConstraints
                .valueOf(jsonExpression.getString(TYPE_CONSTRAINT).toUpperCase());
        final KeyUniquenessConstraint keyUniquenessConstraint = KeyUniquenessConstraint
                .of(jsonExpression.getString(KEY_UNIQUENESS_CONSTRAINT));
        return new SqlPredicateIsNotJson(expression, typeConstraint, keyUniquenessConstraint);
    }

    private SqlNode parsePredicateInConstlist(final JsonObject exp) {
        final SqlNode inExp = parseExpression(exp.getJsonObject(EXPRESSION));
        final List<SqlNode> inArguments = getListOfSqlNodes(exp, ARGUMENTS);
        return new SqlPredicateInConstList(inExp, inArguments);
    }

    private SqlNode parseFunctionScalar(final JsonObject exp) {
        final String functionName = exp.getString(NAME);
        final List<SqlNode> arguments = getListOfSqlNodes(exp, ARGUMENTS);
        return new SqlFunctionScalar(fromScalarFunctionName(functionName), arguments);
    }

    private SqlNode parseFunctionScalarExtract(final JsonObject expression) {
        final SqlNode argument = getSingleArgument(expression);
        final String toExtract = expression.getString(TO_EXTRACT);
        return new SqlFunctionScalarExtract(SqlFunctionScalarExtract.ExtractParameter.valueOf(toExtract), argument);
    }

    private SqlNode parseFunctionScalarCase(final JsonObject exp) {
        final List<SqlNode> caseArguments = getListOfSqlNodes(exp, ARGUMENTS);
        final List<SqlNode> caseResults = getListOfSqlNodes(exp, RESULTS);
        SqlNode caseBasis = null;
        if (exp.containsKey(BASIS)) {
            caseBasis = parseExpression(exp.getJsonObject(BASIS));
        }
        return new SqlFunctionScalarCase(caseArguments, caseResults, caseBasis);
    }

    private SqlNode parseFunctionScalarCast(final JsonObject expression) {
        final DataType castDataType = getDataType(expression.getJsonObject(DATA_TYPE));
        final SqlNode argument = getSingleArgument(expression);
        return new SqlFunctionScalarCast(castDataType, argument);
    }

    private SqlNode parseFunctionScalarJsonValue(final JsonObject jsonObject) {
        final String functionName = jsonObject.getString(NAME);
        final List<SqlNode> arguments = getListOfSqlNodes(jsonObject, ARGUMENTS);
        final DataType returningDataType = getDataType(jsonObject.getJsonObject(RETURNING_DATA_TYPE));
        final SqlFunctionScalarJsonValue.Behavior emptyBehavior = getScalarJsonValueBehavior(jsonObject,
                EMPTY_BEHAVIOR);
        final SqlFunctionScalarJsonValue.Behavior errorBehavior = getScalarJsonValueBehavior(jsonObject,
                ERROR_BEHAVIOR);
        return new SqlFunctionScalarJsonValue(fromScalarFunctionName(functionName), arguments, returningDataType,
                emptyBehavior, errorBehavior);
    }

    private SqlFunctionScalarJsonValue.Behavior getScalarJsonValueBehavior(final JsonObject jsonObject,
            final String key) {
        final JsonObject behaviorJson = jsonObject.getJsonObject(key);
        final String behaviorTypeString = behaviorJson.getString(TYPE);
        final SqlFunctionScalarJsonValue.BehaviorType behaviorType = SqlFunctionScalarJsonValue.BehaviorType
                .valueOf(behaviorTypeString);
        final Optional<SqlNode> expression = getScalarJsonValueExpression(behaviorJson, behaviorType);
        return new SqlFunctionScalarJsonValue.Behavior(behaviorType, expression);
    }

    private Optional<SqlNode> getScalarJsonValueExpression(final JsonObject jsonObject,
            final SqlFunctionScalarJsonValue.BehaviorType behaviorType) {
        if (behaviorType == SqlFunctionScalarJsonValue.BehaviorType.DEFAULT) {
            return Optional.of(parseExpression(jsonObject.getJsonObject(EXPRESSION)));
        } else {
            return Optional.empty();
        }
    }

    private SqlNode parseFunctionAggregate(final JsonObject exp) {
        final String setFunctionName = exp.getString(NAME);
        final List<SqlNode> setArguments = getListOfSqlNodes(exp, ARGUMENTS);
        final boolean distinct = exp.containsKey(DISTINCT) && exp.getBoolean(DISTINCT);
        return new SqlFunctionAggregate(fromAggregationFunctionName(setFunctionName), setArguments, distinct);
    }

    private SqlNode parseFunctionAggregateGroupConcat(final JsonObject expression) {
        final SqlNode argument = getSingleArgument(expression);
        final SqlFunctionAggregateGroupConcat.Builder builder = SqlFunctionAggregateGroupConcat.builder(argument);
        if (expression.containsKey(DISTINCT)) {
            builder.distinct(expression.getBoolean(DISTINCT));
        }
        if (expression.containsKey(ORDER_BY)) {
            builder.orderBy(parseOrderBy(expression.getJsonArray(ORDER_BY)));
        }
        if (expression.containsKey(SEPARATOR)) {
            final SqlLiteralString separator = getSeparator(expression);
            builder.separator(separator);
        }
        return builder.build();
    }

    private SqlNode getSingleArgument(final JsonObject expression) {
        final List<JsonObject> arguments = expression.getJsonArray(ARGUMENTS).getValuesAs(JsonObject.class);
        return parseExpression(arguments.get(0));
    }

    private SqlLiteralString getSeparator(final JsonObject expression) {
        final JsonValue jsonSeparator = expression.get(SEPARATOR);
        if (jsonSeparator.getValueType() == JsonValue.ValueType.STRING) {
            return new SqlLiteralString(expression.getString(SEPARATOR));
        } else {
            return (SqlLiteralString) parseExpression(expression.getJsonObject(SEPARATOR));
        }
    }

    private SqlNode parseFunctionAggregateListagg(final JsonObject expression) {
        final SqlNode argument = getSingleArgument(expression);
        final Behavior overflowBehavior = parseOverflowBehavior(expression);
        final Builder builder = SqlFunctionAggregateListagg.builder(argument, overflowBehavior);
        if (expression.containsKey(DISTINCT)) {
            builder.distinct(expression.getBoolean(DISTINCT));
        }
        if (expression.containsKey(ORDER_BY)) {
            builder.orderBy(parseOrderBy(expression.getJsonArray(ORDER_BY)));
        }
        if (expression.containsKey(SEPARATOR)) {
            final SqlLiteralString separator = (SqlLiteralString) parseExpression(expression.getJsonObject(SEPARATOR));
            builder.separator(separator);
        }
        return builder.build();
    }

    private Behavior parseOverflowBehavior(final JsonObject expression) {
        final JsonObject overflowBehaviorJson = expression.getJsonObject(OVERFLOW_BEHAVIOUR);
        final BehaviorType behaviorType = BehaviorType.valueOf(overflowBehaviorJson.getString(TYPE).toUpperCase());
        final Behavior overflowBehavior = new Behavior(behaviorType);
        if (behaviorType == BehaviorType.TRUNCATE) {
            overflowBehavior.setTruncationType(
                    TruncationType.parseTruncationType(overflowBehaviorJson.getString(TRUNCATION_TYPE)));
            if (overflowBehaviorJson.containsKey(TRUNCATION_FILLER)) {
                final SqlLiteralString truncationFiller = (SqlLiteralString) parseExpression(
                        overflowBehaviorJson.getJsonObject(TRUNCATION_FILLER));
                overflowBehavior.setTruncationFiller(truncationFiller);
            }
        }
        return overflowBehavior;
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
        throw new IllegalStateException(ExaError.messageBuilder("E-VSCOMJAVA-14").message(
                "Could not find table metadata for involved table \"{{tableName|uq}}\". All involved tables: {{involvedTables}}")
                .parameter("tableName", tableName).parameter("involvedTables", this.involvedTablesMetadata.toString())
                .toString());
    }

    private ColumnMetadata findColumnMetadata(final String tableName, final String columnName) {
        final TableMetadata tableMetadata = findInvolvedTableMetadata(tableName);
        for (final ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
            if (columnMetadata.getName().equals(columnName)) {
                return columnMetadata;
            }
        }
        throw new IllegalStateException(ExaError.messageBuilder("E-VSCOMJAVA-15").message(
                "Could not find column metadata for involved table \"{{tableName|uq}}\" and column \"{{columnName|uq}}\". "
                        + "All involved tables: {{involvedTables}}.")
                .parameter("tableName", tableName).parameter("columnName", columnName)
                .parameter("involvedTables", this.involvedTablesMetadata.toString()).toString());
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
