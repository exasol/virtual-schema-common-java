package com.exasol.adapter.request.parser;

import static com.exasol.adapter.metadata.DataType.createDecimal;
import static com.exasol.adapter.metadata.DataType.createVarChar;
import static com.exasol.adapter.metadata.DataType.ExaCharset.UTF8;
import static com.exasol.adapter.sql.SqlFunctionAggregateListagg.BehaviorType.TRUNCATE;
import static com.exasol.adapter.sql.SqlNodeType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.json.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.sql.*;

class PushDownSqlParserTest {
    private PushdownSqlParser defaultParser;

    @BeforeEach
    void beforeEach() {
        final List<TableMetadata> involvedTables = new ArrayList<>();
        final List<ColumnMetadata> defaultColumnMetadata = createDefaultColumnMetadata();
        final TableMetadata tableMetadata = new TableMetadata("CLICKS", "", defaultColumnMetadata, "");
        involvedTables.add(tableMetadata);
        this.defaultParser = PushdownSqlParser.createWithTablesMetadata(involvedTables);
    }

    private List<ColumnMetadata> createDefaultColumnMetadata() {
        final List<ColumnMetadata> columnMetadataList = new ArrayList<>();
        final ColumnMetadata columnMetadata = ColumnMetadata.builder().name("USER_ID").adapterNotes("")
                .type(createDecimal(18, 0)).nullable(true).identity(false).defaultValue("").comment("").build();
        columnMetadataList.add(columnMetadata);
        return columnMetadataList;
    }

    private JsonObject createJsonObjectFromString(final String json) {
        try (final JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.readObject();
        }
    }

    @Test
    void testParseSelectWithGroupBy() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "   \"aggregationType\" : \"group_by\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CLICKS\", " //
                + "        \"alias\" :  \"A\" " //
                + "   }, " //
                + "   \"groupBy\" : [ " //
                + "   { " //
                + "        \"type\" : \"column\", " //
                + "        \"name\" :  \"USER_ID\", " //
                + "        \"columnNr\" : 1, " //
                + "        \"tableName\" : \"CLICKS\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) this.defaultParser
                .parseExpression(jsonObject);
        final SqlGroupBy sqlGroupBy = (SqlGroupBy) sqlStatementSelect.getGroupBy();
        assertAll(() -> assertThat(sqlGroupBy, instanceOf(SqlGroupBy.class)),
                () -> assertThat(sqlGroupBy.getType(), equalTo(GROUP_BY)),
                () -> assertThat(sqlStatementSelect.getType(), equalTo(SELECT)));
    }

    @Test
    void testParseSelectWithLimit() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CLICKS\" " //
                + "   }, " //
                + "   \"limit\" :  " //
                + "   { " //
                + "        \"numElements\" : 10 " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLimit sqlLimit = sqlStatementSelect.getLimit();
        assertAll(() -> assertThat(sqlLimit, instanceOf(SqlLimit.class)),
                () -> assertThat(sqlLimit.getType(), equalTo(LIMIT)),
                () -> assertThat(sqlStatementSelect.getType(), equalTo(SELECT)));
    }

    @Test
    void testParseColumn() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"column\"," //
                + "   \"name\" : \"USER_ID\"," //
                + "   \"tableAlias\" : \"A\"," //
                + "   \"columnNr\" : 1, " //
                + "   \"tableName\" : \"CLICKS\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlColumn sqlColumn = (SqlColumn) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlColumn.getName(), equalTo("USER_ID")),
                () -> assertThat(sqlColumn.getTableName(), equalTo("CLICKS")),
                () -> assertThat(sqlColumn.getId(), equalTo(1)),
                () -> assertThat(sqlColumn.getType(), equalTo(SqlNodeType.COLUMN)));
    }

    @Test
    void testParseLiteralBool() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_bool\", " //
                + "   \"value\" : true " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralBool sqlLiteralBool = (SqlLiteralBool) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralBool.getType(), equalTo(LITERAL_BOOL)),
                () -> assertThat(sqlLiteralBool.getValue(), equalTo(true)));
    }

    @Test
    void testParseLiteralNull() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_null\"" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralNull sqlLiteralNull = (SqlLiteralNull) this.defaultParser.parseExpression(jsonObject);
        assertThat(sqlLiteralNull.getType(), equalTo(LITERAL_NULL));
    }

    @Test
    void testParseLiteralDate() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_date\", " //
                + "   \"value\" : \"2015-12-01\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralDate sqlLiteralDate = (SqlLiteralDate) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralDate.getType(), equalTo(LITERAL_DATE)),
                () -> assertThat(sqlLiteralDate.getValue(), equalTo("2015-12-01")));
    }

    @Test
    void testParseLiteralIntervalDayToSeconds() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_interval\", " //
                + "   \"dataType\" : { " //
                + "        \"type\" : \"INTERVAL\", " //
                + "        \"fromTo\" :  \"DAY TO SECONDS\"" //
                + "   }, " //
                + "   \"value\" : \"2015-12-01\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralInterval sqlLiteralInterval = (SqlLiteralInterval) this.defaultParser
                .parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralInterval.getType(), equalTo(LITERAL_INTERVAL)),
                () -> assertThat(sqlLiteralInterval.getValue(), equalTo("2015-12-01")));
    }

    @Test
    void testParseLiteralTimestamp() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamp\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralTimestamp sqlLiteralTimestamp = (SqlLiteralTimestamp) this.defaultParser
                .parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralTimestamp.getType(), equalTo(LITERAL_TIMESTAMP)),
                () -> assertThat(sqlLiteralTimestamp.getValue(), equalTo("2015-12-01 12:01:01.1234")));
    }

    @Test
    void testParseLiteralTimestamputc() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamputc\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralTimestampUtc sqlLiteralTimestampUtc = (SqlLiteralTimestampUtc) this.defaultParser
                .parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralTimestampUtc.getType(), equalTo(LITERAL_TIMESTAMPUTC)),
                () -> assertThat(sqlLiteralTimestampUtc.getValue(), equalTo("2015-12-01 12:01:01.1234")));
    }

    @ParameterizedTest
    @CsvSource({ "1.0, 1E0", //
            "1.234, 1.234E0", //
            "345600000000, 3.456E11", //
            "0.0000000000000000009236, 9.236E-19", //
            "0.00098, 9.8E-4", //
            "1.2345000000000000e+02, 1.2345E2" //
    })
    void testParseLiteralDouble(final String input, final String expected) {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_double\", " //
                + "   \"value\" : \"" + input + "\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralDouble.getType(), equalTo(LITERAL_DOUBLE)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(expected)));
    }

    @ParameterizedTest
    @CsvSource({ "1E-36, 0.000000000000000000000000000000000001", //
            "12345, 12345", //
            "3.456e11, 345600000000", //
            "9.8e-4, 0.00098", //
            "9.236E-19, 0.0000000000000000009236", //
            "123.45, 123.45", //
            "12345, 12345" //
    })
    void testParseLiteralExactNumeric(final String input, final String expected) {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_exactnumeric\", " //
                + "   \"value\" : \"" + input + "\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralExactnumeric sqlLiteralExactnumeric = (SqlLiteralExactnumeric) this.defaultParser
                .parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralExactnumeric.getType(), equalTo(LITERAL_EXACTNUMERIC)),
                () -> assertThat(sqlLiteralExactnumeric.getValue(), equalTo(expected)));
    }

    @Test
    void testParseLiteralString() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_string\", " //
                + "   \"value\" : \"my string\" " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlLiteralString sqlLiteralString = (SqlLiteralString) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlLiteralString.getType(), equalTo(LITERAL_STRING)),
                () -> assertThat(sqlLiteralString.getValue(), equalTo("my string")));
    }

    @Test
    void testParsePredicateAnd() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_and\", " //
                + "   \"expressions\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"2.0\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateAnd sqlPredicateAnd = (SqlPredicateAnd) this.defaultParser.parseExpression(jsonObject);
        final List<SqlNode> expressions = sqlPredicateAnd.getAndedPredicates();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) expressions.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) expressions.get(1);
        assertAll(() -> assertThat(sqlPredicateAnd.getType(), equalTo(PREDICATE_AND)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo("1E0")), //
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo("2E0")));
    }

    @Test
    void testParsePredicateAndEmptyExpressions() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_and\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateAnd sqlPredicateAnd = (SqlPredicateAnd) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlPredicateAnd.getType(), equalTo(PREDICATE_AND)),
                () -> assertThat(sqlPredicateAnd.getAndedPredicates(), is(empty())));
    }

    @Test
    void testParsePredicateOr() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_or\", " //
                + "   \"expressions\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"2.0\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateOr sqlPredicateAnd = (SqlPredicateOr) this.defaultParser.parseExpression(jsonObject);
        final List<SqlNode> expressions = sqlPredicateAnd.getOrPredicates();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) expressions.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) expressions.get(1);
        assertAll(() -> assertThat(sqlPredicateAnd.getType(), equalTo(PREDICATE_OR)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo("1E0")), //
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo("2E0")));
    }

    @Test
    void testParsePredicateOrEmptyExpressions() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_or\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateOr sqlPredicateOr = (SqlPredicateOr) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlPredicateOr.getType(), equalTo(PREDICATE_OR)),
                () -> assertThat(sqlPredicateOr.getOrPredicates(), is(empty())));
    }

    @Test
    void testParsePredicateNot() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_not\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_null\"" //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateNot sqlPredicateNot = (SqlPredicateNot) this.defaultParser.parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlPredicateNot.getType(), equalTo(PREDICATE_NOT)),
                () -> assertThat(sqlPredicateNot.getExpression(), instanceOf(SqlLiteralNull.class)));
    }

    @Test
    void testParsePredicateEqual() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_equal\", " //
                + "   \"left\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   }, " //
                + "   \"right\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateEqual sqlPredicateEqual = (SqlPredicateEqual) this.defaultParser.parseExpression(jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateEqual.getType(), equalTo(PREDICATE_EQUAL)),
                () -> assertThat(left.getValue(), equalTo("1.234E0")), //
                () -> assertThat(right.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateNotEqual() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_notequal\", " //
                + "   \"left\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.2\" " //
                + "   }, " //
                + "   \"right\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateNotEqual sqlPredicateNotEqual = (SqlPredicateNotEqual) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateNotEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateNotEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateNotEqual.getType(), equalTo(PREDICATE_NOTEQUAL)),
                () -> assertThat(left.getValue(), equalTo("1.2E0")), //
                () -> assertThat(right.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateLess() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_less\", " //
                + "   \"left\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.2\" " //
                + "   }, " //
                + "   \"right\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateLess sqlPredicateLess = (SqlPredicateLess) this.defaultParser.parseExpression(jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLess.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLess.getRight();
        assertAll(() -> assertThat(sqlPredicateLess.getType(), equalTo(PREDICATE_LESS)),
                () -> assertThat(left.getValue(), equalTo("1.2E0")), //
                () -> assertThat(right.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateLessEqual() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_lessequal\", " //
                + "   \"left\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.2\" " //
                + "   }, " //
                + "   \"right\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateLessEqual sqlPredicateLessEqual = (SqlPredicateLessEqual) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLessEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLessEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateLessEqual.getType(), equalTo(PREDICATE_LESSEQUAL)),
                () -> assertThat(left.getValue(), equalTo("1.2E0")), //
                () -> assertThat(right.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateLike() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_like\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"abcd\" " //
                + "   }, " //
                + "   \"pattern\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"a_d\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateLike sqlPredicateLike = (SqlPredicateLike) this.defaultParser.parseExpression(jsonObject);
        final SqlLiteralString left = (SqlLiteralString) sqlPredicateLike.getLeft();
        final SqlLiteralString pattern = (SqlLiteralString) sqlPredicateLike.getPattern();
        assertAll(() -> assertThat(sqlPredicateLike.getType(), equalTo(PREDICATE_LIKE)),
                () -> assertThat(left.getValue(), equalTo("abcd")), //
                () -> assertThat(pattern.getValue(), equalTo("a_d")));
    }

    @Test
    void testParsePredicateLikeRegexp() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_like_regexp\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"abcd\" " //
                + "   }, " //
                + "   \"pattern\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"a_d\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateLikeRegexp sqlPredicateLike = (SqlPredicateLikeRegexp) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralString left = (SqlLiteralString) sqlPredicateLike.getLeft();
        final SqlLiteralString pattern = (SqlLiteralString) sqlPredicateLike.getPattern();
        assertAll(() -> assertThat(sqlPredicateLike.getType(), equalTo(PREDICATE_LIKE_REGEXP)),
                () -> assertThat(left.getValue(), equalTo("abcd")), //
                () -> assertThat(pattern.getValue(), equalTo("a_d")));
    }

    @Test
    void testParsePredicateBetween() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_between\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.2\" " //
                + "   }, " //
                + "   \"left\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.1\" " //
                + "   }, " //
                + "   \"right\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateBetween sqlPredicateLike = (SqlPredicateBetween) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLike.getBetweenLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLike.getBetweenRight();
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateLike.getExpression();
        assertAll(() -> assertThat(sqlPredicateLike.getType(), equalTo(PREDICATE_BETWEEN)),
                () -> assertThat(left.getValue(), equalTo("1.1E0")), //
                () -> assertThat(expression.getValue(), equalTo("1.2E0")), //
                () -> assertThat(right.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateIsNull() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_null\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"0.0\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateIsNull sqlPredicateIsNull = (SqlPredicateIsNull) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateIsNull.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsNull.getType(), equalTo(PREDICATE_IS_NULL)),
                () -> assertThat(expression.getValue(), equalTo("0E0")));
    }

    @Test
    void testParsePredicateIsNotNull() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_not_null\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateIsNotNull sqlPredicateIsNotNull = (SqlPredicateIsNotNull) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateIsNotNull.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsNotNull.getType(), equalTo(PREDICATE_IS_NOT_NULL)),
                () -> assertThat(expression.getValue(), equalTo("1E0")));
    }

    @Test
    void testParseFunctionScalar() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_scalar\", " //
                + "   \"numArgs\" : 1, " //
                + "   \"name\" : \"ABS\", " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionScalar sqlFunctionScalar = (SqlFunctionScalar) this.defaultParser.parseExpression(jsonObject);
        final List<SqlNode> expressions = sqlFunctionScalar.getArguments();
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) expressions.get(0);
        assertAll(() -> assertThat(sqlFunctionScalar.getType(), equalTo(FUNCTION_SCALAR)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo("1E0")));
    }

    @Test
    void testParseFunctionScalarExtract() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_scalar_extract\", " //
                + "   \"name\" : \"EXTRACT\", " //
                + "   \"toExtract\" : \"MINUTE\", " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_timestamp\", " //
                + "        \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionScalarExtract sqlFunctionScalarExtract = (SqlFunctionScalarExtract) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralTimestamp sqlLiteralTimestamp = (SqlLiteralTimestamp) sqlFunctionScalarExtract.getArgument();
        assertAll(() -> assertThat(sqlFunctionScalarExtract.getType(), equalTo(FUNCTION_SCALAR_EXTRACT)),
                () -> assertThat(sqlFunctionScalarExtract.getToExtract(), equalTo("MINUTE")), //
                () -> assertThat(sqlLiteralTimestamp.getValue(), equalTo("2015-12-01 12:01:01.1234")));
    }

    @Test
    void testParseFunctionScalarCast() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_scalar_cast\", " //
                + "   \"name\" : \"CAST\", " //
                + "   \"dataType\" : { " //
                + "        \"type\" : \"VARCHAR\", " //
                + "        \"size\" : 10000 " //
                + "   }, " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.234\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionScalarCast sqlFunctionScalarCast = (SqlFunctionScalarCast) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionScalarCast.getArgument();
        assertAll(() -> assertThat(sqlFunctionScalarCast.getType(), equalTo(FUNCTION_SCALAR_CAST)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo("1.234E0")));
    }

    @Test
    void testParsePredicateInConstlist() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_in_constlist\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"2.0\" " //
                + "   }, " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"2.0\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateInConstList sqlPredicateInConstList = (SqlPredicateInConstList) this.defaultParser
                .parseExpression(jsonObject);
        final List<SqlNode> arguments = sqlPredicateInConstList.getInArguments();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) arguments.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) arguments.get(1);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateInConstList.getExpression();
        assertAll(() -> assertThat(sqlPredicateInConstList.getType(), equalTo(PREDICATE_IN_CONSTLIST)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo("1E0")),
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo("2E0")),
                () -> assertThat(expression.getValue(), equalTo("2E0")));
    }

    @Test
    void testParsePredicateIsJson() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_json\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'123'\"" //
                + "   }, " //
                + "   \"typeConstraint\" : \"VALUE\", " //
                + "   \"keyUniquenessConstraint\" : \"WITHOUT UNIQUE KEYS\"" + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateIsJson sqlPredicateIsJson = (SqlPredicateIsJson) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralString expression = (SqlLiteralString) sqlPredicateIsJson.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsJson.getType(), equalTo(PREDICATE_IS_JSON)),
                () -> assertThat(expression.getValue(), equalTo("'123'")),
                () -> assertThat(sqlPredicateIsJson.getTypeConstraint(), equalTo("VALUE")),
                () -> assertThat(sqlPredicateIsJson.getKeyUniquenessConstraint(), equalTo("WITHOUT UNIQUE KEYS")));
    }

    @Test
    void testParsePredicateIsNotJson() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_not_json\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'123'\"" //
                + "   }, " //
                + "   \"typeConstraint\" : \"VALUE\", " //
                + "   \"keyUniquenessConstraint\" : \"WITHOUT UNIQUE KEYS\"" + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlPredicateIsNotJson sqlPredicateIsNotJson = (SqlPredicateIsNotJson) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralString expression = (SqlLiteralString) sqlPredicateIsNotJson.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsNotJson.getType(), equalTo(PREDICATE_IS_NOT_JSON)),
                () -> assertThat(expression.getValue(), equalTo("'123'")),
                () -> assertThat(sqlPredicateIsNotJson.getTypeConstraint(), equalTo("VALUE")),
                () -> assertThat(sqlPredicateIsNotJson.getKeyUniquenessConstraint(), equalTo("WITHOUT UNIQUE KEYS")));
    }

    @Test
    void testParseFunctionScalarCase() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_scalar_case\", " //
                + "   \"name\" : \"CASE\", " //
                + "   \"basis\" : { " //
                + "        \"type\" : \"column\", " //
                + "        \"columnNr\" : 1, " //
                + "        \"name\" : \"USER_ID\", " //
                + "        \"tableName\" : \"CLICKS\" " //
                + "   }, " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_exactnumeric\", " //
                + "        \"value\" : \"1\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_exactnumeric\", " //
                + "        \"value\" : \"2\" " //
                + "   } " //
                + "   ], " //
                + "   \"results\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"VERY GOOD\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"GOOD\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionScalarCase sqlFunctionScalarCase = (SqlFunctionScalarCase) this.defaultParser
                .parseExpression(jsonObject);
        final List<SqlNode> arguments = sqlFunctionScalarCase.getArguments();
        final SqlLiteralExactnumeric sqlLiteralExactnumeric1 = (SqlLiteralExactnumeric) arguments.get(0);
        final SqlLiteralExactnumeric sqlLiteralExactnumeric2 = (SqlLiteralExactnumeric) arguments.get(1);
        final List<SqlNode> results = sqlFunctionScalarCase.getResults();
        final SqlLiteralString sqlLiteralString1 = (SqlLiteralString) results.get(0);
        final SqlLiteralString sqlLiteralString2 = (SqlLiteralString) results.get(1);
        assertAll(() -> assertThat(sqlFunctionScalarCase.getType(), equalTo(FUNCTION_SCALAR_CASE)),
                () -> assertThat(sqlFunctionScalarCase.getBasis(), instanceOf(SqlNode.class)),
                () -> assertThat(sqlLiteralExactnumeric1.getValue(), equalTo("1")),
                () -> assertThat(sqlLiteralExactnumeric2.getValue(), equalTo("2")),
                () -> assertThat(sqlLiteralString1.getValue(), equalTo("VERY GOOD")),
                () -> assertThat(sqlLiteralString2.getValue(), equalTo("GOOD")));
    }

    @Test
    void testParseFunctionScalarJsonValue() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_scalar_json_value\", " //
                + "   \"name\" : \"JSON_VALUE\", " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'first argument'\"" //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'second argument'\"" ///
                + "   } " //
                + "   ], " //
                + "   \"returningDataType\" : { " //
                + "        \"type\" : \"VARCHAR\", " //
                + "        \"size\" : 10000 " //
                + "   }, " //
                + "   \"emptyBehavior\" : " //
                + "   { " //
                + "        \"type\" : \"NULL\" " //
                + "   }, " //
                + "   \"errorBehavior\" : " //
                + "   { " //
                + "        \"type\" : \"DEFAULT\", " //
                + "         \"expression\" : " //
                + "     { " //
                + "            \"type\" : \"literal_string\", " //
                + "            \"value\" : \"*** error ***\" " //
                + "      } " //
                + "   } " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionScalarJsonValue sqlFunctionScalarJsonValue = (SqlFunctionScalarJsonValue) this.defaultParser
                .parseExpression(jsonObject);
        final ScalarFunction scalarFunction = sqlFunctionScalarJsonValue.getScalarFunction();
        final List<SqlNode> arguments = sqlFunctionScalarJsonValue.getArguments();
        final SqlLiteralString firstArgument = (SqlLiteralString) arguments.get(0);
        final SqlLiteralString secondArgument = (SqlLiteralString) arguments.get(1);
        final DataType returningDataType = sqlFunctionScalarJsonValue.getReturningDataType();
        final SqlFunctionScalarJsonValue.Behavior emptyBehavior = sqlFunctionScalarJsonValue.getEmptyBehavior();
        final SqlFunctionScalarJsonValue.Behavior errorBehavior = sqlFunctionScalarJsonValue.getErrorBehavior();
        assertAll(() -> assertThat(sqlFunctionScalarJsonValue.getType(), equalTo(FUNCTION_SCALAR_JSON_VALUE)),
                () -> assertThat(scalarFunction, equalTo(ScalarFunction.JSON_VALUE)),
                () -> assertThat(firstArgument.getValue(), equalTo("'first argument'")),
                () -> assertThat(secondArgument.getValue(), equalTo("'second argument'")),
                () -> assertThat(returningDataType, equalTo(createVarChar(10000, UTF8))),
                () -> assertThat(emptyBehavior,
                        equalTo(new SqlFunctionScalarJsonValue.Behavior(SqlFunctionScalarJsonValue.BehaviorType.NULL,
                                Optional.empty()))),
                () -> assertThat(errorBehavior,
                        equalTo(new SqlFunctionScalarJsonValue.Behavior(SqlFunctionScalarJsonValue.BehaviorType.DEFAULT,
                                Optional.of(new SqlLiteralString("*** error ***"))))));
    }

    @Test
    void testParseFunctionAggregate() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_aggregate\", " //
                + "   \"name\" : \"SUM\", " //
                + "   \"arguments\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   }, " //
                + "   { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"2.0\" " //
                + "   } " //
                + "   ] " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionAggregate sqlFunctionAggregate = (SqlFunctionAggregate) this.defaultParser
                .parseExpression(jsonObject);
        final List<SqlNode> arguments = sqlFunctionAggregate.getArguments();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) arguments.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) arguments.get(1);
        assertAll(() -> assertThat(sqlFunctionAggregate.getType(), equalTo(FUNCTION_AGGREGATE)),
                () -> assertThat(sqlFunctionAggregate.getFunctionName(), equalTo("SUM")), //
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo("1E0")),
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo("2E0")));
    }

    @Test
    void testParseFunctionAggregateGroupConcatOldApi() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_aggregate_group_concat\", " //
                + "   \"name\" : \"GROUP_CONCAT\", " //
                + "   \"distinct\" : true, " //
                + "   \"arguments\" : [ " //
                + "     { " //
                + "          \"type\" : \"literal_double\", " //
                + "          \"value\" : \"2.0\" " //
                + "     } " //
                + "     ], " //
                + "     \"orderBy\" : [ " //
                + "     { " //
                + "          \"type\" : \"order_by_element\", " //
                + "          \"expression\" : " //
                + "     { " //
                + "          \"type\" : \"column\", " //
                + "          \"columnNr\" : 1, " //
                + "          \"name\" : \"USER_ID\", " //
                + "          \"tableName\" : \"CLICKS\" " //
                + "     }, " //
                + "     \"isAscending\" : true, " //
                + "     \"nullsLast\" : true " //
                + "     } " //
                + "   ], " //
                + "   \"separator\": \",\"  " //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat = (SqlFunctionAggregateGroupConcat) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionAggregateGroupConcat.getArgument();
        assertAll(() -> assertThat(sqlFunctionAggregateGroupConcat.getType(), equalTo(FUNCTION_AGGREGATE_GROUP_CONCAT)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.getFunctionName(), equalTo("GROUP_CONCAT")), //
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo("2E0")));
    }

    @Test
    void testParseFunctionAggregateGroupConcatNewApi() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"function_aggregate_group_concat\", " //
                + "   \"name\" : \"GROUP_CONCAT\", " //
                + "   \"distinct\" : true, " //
                + "   \"arguments\" : [ " //
                + "     { " //
                + "          \"type\" : \"literal_double\", " //
                + "          \"value\" : \"2.0\" " //
                + "     } " //
                + "     ], " //
                + "     \"orderBy\" : [ " //
                + "     { " //
                + "          \"type\" : \"order_by_element\", " //
                + "          \"expression\" : " //
                + "     { " //
                + "          \"type\" : \"column\", " //
                + "          \"columnNr\" : 1, " //
                + "          \"name\" : \"USER_ID\", " //
                + "          \"tableName\" : \"CLICKS\" " //
                + "     }, " //
                + "     \"isAscending\" : true, " //
                + "     \"nullsLast\" : true " //
                + "     } " //
                + "   ], " //
                + "    \"separator\":" //
                + "    {" //
                + "        \"type\": \"literal_string\"," //
                + "        \"value\": \", \"" //
                + "    }" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat = (SqlFunctionAggregateGroupConcat) this.defaultParser
                .parseExpression(jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionAggregateGroupConcat.getArgument();
        assertAll(() -> assertThat(sqlFunctionAggregateGroupConcat.getType(), equalTo(FUNCTION_AGGREGATE_GROUP_CONCAT)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.getFunctionName(), equalTo("GROUP_CONCAT")), //
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo("2E0")),
                () -> assertThat(sqlFunctionAggregateGroupConcat.getSeparator(), equalTo(new SqlLiteralString(", "))),
                () -> assertThat(sqlFunctionAggregateGroupConcat.hasDistinct(), equalTo(true)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.hasOrderBy(), equalTo(true)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.hasSeparator(), equalTo(true)));
    }

    @Test
    void testParseFunctionAggregateGroupListagg() {
        final String sqlAsJson = "{" //
                + "    \"type\": \"function_aggregate_listagg\"," //
                + "    \"name\": \"LISTAGG\"," //
                + "    \"distinct\": true," //
                + "    \"arguments\": [" //
                + "    {" //
                + "        \"type\": \"column\"," //
                + "        \"columnNr\": 1," //
                + "        \"name\": \"USER_ID\"," //
                + "        \"tableName\": \"CLICKS\"" //
                + "    }" //
                + "    ]," //
                + "    \"separator\":" //
                + "    {" //
                + "        \"type\": \"literal_string\"," //
                + "        \"value\": \", \"" //
                + "    }," //
                + "    \"overflowBehavior\":" //
                + "    {" //
                + "        \"type\": \"TRUNCATE\"," //
                + "        \"truncationType\": \"WITH COUNT\"," //
                + "        \"truncationFiller\": " //
                + "        {" //
                + "             \"type\": \"literal_string\"," //
                + "             \"value\": \"filler\"" //
                + "         }" //
                + "    }," //
                + "    \"orderBy\": [" //
                + "        {" //
                + "            \"type\": \"order_by_element\"," //
                + "            \"expression\":" //
                + "            {" //
                + "              \"type\": \"column\"," //
                + "               \"columnNr\": 1," //
                + "                \"name\": \"USER_ID\"," //
                + "                \"tableName\": \"CLICKS\"" //
                + "            }," //
                + "            \"isAscending\": true," //
                + "            \"nullsLast\": true" //
                + "        }" //
                + "    ]" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlFunctionAggregateListagg listagg = (SqlFunctionAggregateListagg) this.defaultParser
                .parseExpression(jsonObject);
        final SqlColumn sqlColumn = (SqlColumn) listagg.getArgument();
        assertAll(() -> assertThat(listagg.getType(), equalTo(FUNCTION_AGGREGATE_LISTAGG)),
                () -> assertThat(listagg.getFunctionName(), equalTo("LISTAGG")), //
                () -> assertThat(listagg.hasDistinct(), equalTo(true)), //
                () -> assertThat(listagg.hasOrderBy(), equalTo(true)), //
                () -> assertThat(listagg.hasSeparator(), equalTo(true)), //
                () -> assertThat(listagg.getSeparator(), equalTo(new SqlLiteralString(", "))), //
                () -> assertThat(listagg.getOrderBy().getType(), equalTo(ORDER_BY)), //
                () -> assertThat(listagg.getOverflowBehavior().getBehaviorType(), equalTo(TRUNCATE)), //
                () -> assertThat(listagg.getOverflowBehavior().getTruncationType(), equalTo("WITH COUNT")), //
                () -> assertThat(listagg.getOverflowBehavior().getTruncationFiller(),
                        equalTo(new SqlLiteralString("filler"))), //
                () -> assertThat(sqlColumn.getId(), equalTo(1)));
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, ID, CUSTOMERS", //
            "1, 1, NAME, CUSTOMERS", //
            "2, 0, CUSTOMER_ID, ORDERS", //
            "3, 1, ITEM_ID, ORDERS" //
    })
    void testSimpleInnerJoinRequestWithExplicitSelectList(final int columnNumber, final int columnId,
            final String columnName, final String tableName) throws IOException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_simple_inner_join_with_select_list.json")));
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        final SqlStatementSelect select = (SqlStatementSelect) pushdownSqlParser.parseExpression(jsonObject);
        final SqlJoin from = (SqlJoin) select.getFromClause();
        final SqlColumn column = (SqlColumn) select.getSelectList().getExpressions().get(columnNumber);
        assertAll(() -> assertThat(SqlNodeType.JOIN, sameInstance(from.getType())),
                () -> assertThat(JoinType.INNER, sameInstance(from.getJoinType())),
                () -> assertThat(SqlNodeType.PREDICATE_EQUAL, sameInstance(from.getCondition().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getLeft().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getRight().getType())),
                () -> assertThat(select.getSelectList().hasExplicitColumnsList(), equalTo(true)),
                () -> assertThat(select.getSelectList().getExpressions().size(), equalTo(4)),
                () -> assertThat(column.getId(), equalTo(columnId)),
                () -> assertThat(column.getName(), equalTo(columnName)),
                () -> assertThat(column.getType(), equalTo(COLUMN)),
                () -> assertThat(column.getTableName(), equalTo(tableName)));
    }

    private PushdownSqlParser getCustomPushdownSqlParserWithTwoTables() {
        final List<TableMetadata> tables = new ArrayList<>();
        final List<ColumnMetadata> columns = List.of( //
                ColumnMetadata.builder().name("ID").adapterNotes("").type(createDecimal(18, 0)).build(), //
                ColumnMetadata.builder().name("NAME").adapterNotes("").type(createVarChar(200, UTF8)).build());
        tables.add(new TableMetadata("CUSTOMERS", "", columns, ""));
        final List<ColumnMetadata> columns2 = List.of(
                ColumnMetadata.builder().name("CUSTOMER_ID").adapterNotes("").type(createDecimal(18, 0)).build(), //
                ColumnMetadata.builder().name("ITEM_ID").adapterNotes("").type(createVarChar(200, UTF8)).build());
        tables.add(new TableMetadata("ORDERS", "", columns2, ""));
        return PushdownSqlParser.createWithTablesMetadata(tables);
    }

    @Test
    void testParseSelectWithoutSelectList() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CUSTOMERS\" " //
                + "   }" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) pushdownSqlParser
                .parseExpression(jsonObject);
        final SqlColumn first = (SqlColumn) sqlStatementSelect.getSelectList().getExpressions().get(0);
        final SqlColumn second = (SqlColumn) sqlStatementSelect.getSelectList().getExpressions().get(1);
        assertAll(() -> assertThat(sqlStatementSelect.getType(), equalTo(SELECT)),
                () -> assertThat(sqlStatementSelect.getSelectList().hasExplicitColumnsList(), equalTo(true)), //
                () -> assertThat(sqlStatementSelect.getSelectList().getExpressions().size(), equalTo(2)), //
                () -> assertThat(first.getName(), equalTo("ID")), //
                () -> assertThat(first.getTableName(), equalTo("CUSTOMERS")), //
                () -> assertThat(second.getName(), equalTo("NAME")), //
                () -> assertThat(second.getTableName(), equalTo("CUSTOMERS")) //
        );
    }

    @Test
    void testParseSelectWithEmptySelectList() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CLICKS\" " //
                + "   }," //
                + "   \"selectList\" : []" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) this.defaultParser
                .parseExpression(jsonObject);
        assertAll(() -> assertThat(sqlStatementSelect.getType(), equalTo(SELECT)),
                () -> assertThat(sqlStatementSelect.getSelectList().hasExplicitColumnsList(), equalTo(false)), //
                () -> assertThat(sqlStatementSelect.getSelectList().getExpressions().isEmpty(), equalTo(true)) //
        );
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, ID, CUSTOMERS", //
            "1, 1, NAME, CUSTOMERS", //
            "2, 0, CUSTOMER_ID, ORDERS", //
            "3, 1, ITEM_ID, ORDERS" //
    })
    void testSimpleInnerJoinRequestWithoutSelectList(final int columnNumber, final int columnId,
            final String columnName, final String tableName) throws IOException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_simple_inner_join_without_select_list.json")));
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        final SqlStatementSelect select = (SqlStatementSelect) pushdownSqlParser.parseExpression(jsonObject);
        final SqlJoin from = (SqlJoin) select.getFromClause();
        final SqlColumn column = (SqlColumn) select.getSelectList().getExpressions().get(columnNumber);
        assertAll(() -> assertThat(SqlNodeType.JOIN, sameInstance(from.getType())),
                () -> assertThat(JoinType.INNER, sameInstance(from.getJoinType())),
                () -> assertThat(SqlNodeType.PREDICATE_EQUAL, sameInstance(from.getCondition().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getLeft().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getRight().getType())),
                () -> assertThat(select.getSelectList().hasExplicitColumnsList(), equalTo(true)),
                () -> assertThat(select.getSelectList().getExpressions().size(), equalTo(4)),
                () -> assertThat(column.getId(), equalTo(columnId)),
                () -> assertThat(column.getName(), equalTo(columnName)),
                () -> assertThat(column.getType(), equalTo(COLUMN)),
                () -> assertThat(column.getTableName(), equalTo(tableName)));
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, ID, CUSTOMERS, T1", //
            "1, 1, NAME, CUSTOMERS, T1", //
            "2, 0, ITEM_ID, ITEMS, ", //
            "3, 1, ITEM_NAME, ITEMS, ", //
            "4, 0, CUSTOMER_ID, ORDERS, ", //
            "5, 1, ITEM_ID, ORDERS, " //
    })
    void testNestedJoinRequestWithoutSelectList(final int columnNumber, final int columnId, final String columnName,
            final String tableName, final String tableAlias) throws IOException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_nested_join_without_select_list.json")));
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithThreeTables();
        final SqlStatementSelect select = (SqlStatementSelect) pushdownSqlParser.parseExpression(jsonObject);
        final SqlColumn column = (SqlColumn) select.getSelectList().getExpressions().get(columnNumber);
        assertAll(() -> assertThat(select.getSelectList().hasExplicitColumnsList(), equalTo(true)),
                () -> assertThat(select.getSelectList().getExpressions().size(), equalTo(6)),
                () -> assertThat(column.getId(), equalTo(columnId)),
                () -> assertThat(column.getName(), equalTo(columnName)),
                () -> assertThat(column.getType(), equalTo(COLUMN)),
                () -> assertThat(column.getTableName(), equalTo(tableName)),
                () -> assertThat(column.getTableAlias(), equalTo(tableAlias)));
    }

    private PushdownSqlParser getCustomPushdownSqlParserWithThreeTables() {
        final List<TableMetadata> tables = new ArrayList<>();
        final List<ColumnMetadata> columns = List.of( //
                ColumnMetadata.builder().name("ID").adapterNotes("").type(createVarChar(200, UTF8)).build(), //
                ColumnMetadata.builder().name("NAME").adapterNotes("").type(createVarChar(200, UTF8)).build());
        tables.add(new TableMetadata("CUSTOMERS", "", columns, ""));
        final List<ColumnMetadata> columns2 = List.of(
                ColumnMetadata.builder().name("CUSTOMER_ID").adapterNotes("").type(createVarChar(200, UTF8)).build(), //
                ColumnMetadata.builder().name("ITEM_ID").adapterNotes("").type(createVarChar(200, UTF8)).build());
        tables.add(new TableMetadata("ORDERS", "", columns2, ""));
        final List<ColumnMetadata> columns3 = List.of(
                ColumnMetadata.builder().name("ITEM_ID").adapterNotes("").type(createVarChar(200, UTF8)).build(), //
                ColumnMetadata.builder().name("ITEM_NAME").adapterNotes("").type(createVarChar(200, UTF8)).build());
        tables.add(new TableMetadata("ITEMS", "", columns3, ""));
        return PushdownSqlParser.createWithTablesMetadata(tables);
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, ID, CUSTOMERS, T1", //
            "1, 1, NAME, CUSTOMERS, T1", //
            "2, 0, ID, CUSTOMERS, ", //
            "3, 1, NAME, CUSTOMERS, ", //
            "4, 0, CUSTOMER_ID, ORDERS, ", //
            "5, 1, ITEM_ID, ORDERS, " //
    })
    void testNestedJoinRequestRepeatedTableWithoutSelectList(final int columnNumber, final int columnId,
            final String columnName, final String tableName, final String tableAlias) throws IOException {
        final String sqlAsJson = new String(Files.readAllBytes(Paths.get(
                "src/test/resources/json/pushdown_request_nested_join_with_repeated_table_without_select_list.json")));
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        final SqlStatementSelect select = (SqlStatementSelect) pushdownSqlParser.parseExpression(jsonObject);
        final SqlColumn column = (SqlColumn) select.getSelectList().getExpressions().get(columnNumber);
        assertAll(() -> assertThat(select.getSelectList().hasExplicitColumnsList(), equalTo(true)),
                () -> assertThat(select.getSelectList().getExpressions().size(), equalTo(6)),
                () -> assertThat(column.getId(), equalTo(columnId)),
                () -> assertThat(column.getName(), equalTo(columnName)),
                () -> assertThat(column.getType(), equalTo(COLUMN)),
                () -> assertThat(column.getTableName(), equalTo(tableName)),
                () -> assertThat(column.getTableAlias(), equalTo(tableAlias)));
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, ITEM_ID, ITEMS, ", //
            "1, 1, ITEM_NAME, ITEMS, ", //
            "2, 0, CUSTOMER_ID, ORDERS, ", //
            "3, 1, ITEM_ID, ORDERS, ", //
            "4, 0, ID, CUSTOMERS, T1", //
            "5, 1, NAME, CUSTOMERS, T1" //
    })
    void testNestedJoinRequestWithoutSelectListReversed(final int columnNumber, final int columnId,
            final String columnName, final String tableName, final String tableAlias) throws IOException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_nested_join_without_select_list_reversed.json")));
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithThreeTables();
        final SqlStatementSelect select = (SqlStatementSelect) pushdownSqlParser.parseExpression(jsonObject);
        final SqlColumn column = (SqlColumn) select.getSelectList().getExpressions().get(columnNumber);
        assertAll(() -> assertThat(select.getSelectList().hasExplicitColumnsList(), equalTo(true)),
                () -> assertThat(select.getSelectList().getExpressions().size(), equalTo(6)),
                () -> assertThat(column.getId(), equalTo(columnId)),
                () -> assertThat(column.getName(), equalTo(columnName)),
                () -> assertThat(column.getType(), equalTo(COLUMN)),
                () -> assertThat(column.getTableName(), equalTo(tableName)),
                () -> assertThat(column.getTableAlias(), equalTo(tableAlias)));
    }

    @Test
    void testParseSelectWithoutSelectListAndInvolvedTablesMetadata() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CUSTOMERS\" " //
                + "   }" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserTableWithoutColumns();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> pushdownSqlParser.parseExpression(jsonObject));
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-28"));
    }

    @Test
    void testParseInvalidExpressionType() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"limit\"" //
                + "}";
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserTableWithoutColumns();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pushdownSqlParser.parseExpression(jsonObject));
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-8"));
    }

    private PushdownSqlParser getCustomPushdownSqlParserTableWithoutColumns() {
        final List<TableMetadata> tables = List.of(new TableMetadata("CUSTOMERS", "", Collections.emptyList(), ""));
        return PushdownSqlParser.createWithTablesMetadata(tables);
    }
}