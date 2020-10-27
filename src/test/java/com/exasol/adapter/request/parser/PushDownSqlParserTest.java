package com.exasol.adapter.request.parser;

import static com.exasol.adapter.sql.SqlFunctionAggregateListagg.BehaviorType.TRUNCATE;
import static com.exasol.adapter.sql.SqlNodeType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.json.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.sql.*;

class PushDownSqlParserTest {
    private JsonObject jsonObject;
    private ColumnMetadata columnMetadata;
    private PushdownSqlParser pushdownSqlParser;

    @BeforeEach
    void SetUp() {
        final List<TableMetadata> involvedTables = new ArrayList<>();
        final List<ColumnMetadata> columnMetadatas = new ArrayList<>();
        this.columnMetadata = ColumnMetadata.builder().name("USER_ID").adapterNotes("")
                .type(DataType.createDecimal(18, 0)).nullable(true).identity(false).defaultValue("").comment("")
                .build();
        columnMetadatas.add(this.columnMetadata);
        final TableMetadata tableMetadata = new TableMetadata("CLICKS", "", columnMetadatas, "");
        involvedTables.add(tableMetadata);
        this.pushdownSqlParser = PushdownSqlParser.createWithTablesMetadata(involvedTables);
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

        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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

        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlStatementSelect sqlStatementSelect = (SqlStatementSelect) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlColumn sqlColumn = (SqlColumn) this.pushdownSqlParser.parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlColumn.getName(), equalTo("USER_ID")),
                () -> assertThat(sqlColumn.getTableName(), equalTo("CLICKS")),
                () -> assertThat(sqlColumn.getId(), equalTo(1)),
                () -> assertThat(sqlColumn.getType(), equalTo(SqlNodeType.COLUMN)),
                () -> assertThat(sqlColumn.getMetadata(), equalTo(this.columnMetadata)));
    }

    @Test
    void testParseLiteralBool() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_bool\", " //
                + "   \"value\" : true " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralBool sqlLiteralBool = (SqlLiteralBool) this.pushdownSqlParser.parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralBool.getType(), equalTo(LITERAL_BOOL)),
                () -> assertThat(sqlLiteralBool.getValue(), equalTo(true)));
    }

    @Test
    void testParseLiteralNull() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_null\"" //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralNull sqlLiteralNull = (SqlLiteralNull) this.pushdownSqlParser.parseExpression(this.jsonObject);
        assertThat(sqlLiteralNull.getType(), equalTo(LITERAL_NULL));
    }

    @Test
    void testParseLiteralDate() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_date\", " //
                + "   \"value\" : \"2015-12-01\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralDate sqlLiteralDate = (SqlLiteralDate) this.pushdownSqlParser.parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralInterval sqlLiteralInterval = (SqlLiteralInterval) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralInterval.getType(), equalTo(LITERAL_INTERVAL)),
                () -> assertThat(sqlLiteralInterval.getValue(), equalTo("2015-12-01")));
    }

    @Test
    void testParseLiteralTimestamp() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamp\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralTimestamp sqlLiteralTimestamp = (SqlLiteralTimestamp) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralTimestamp.getType(), equalTo(LITERAL_TIMESTAMP)),
                () -> assertThat(sqlLiteralTimestamp.getValue(), equalTo("2015-12-01 12:01:01.1234")));
    }

    @Test
    void testParseLiteralTimestamputc() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamputc\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralTimestampUtc sqlLiteralTimestampUtc = (SqlLiteralTimestampUtc) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralTimestampUtc.getType(), equalTo(LITERAL_TIMESTAMPUTC)),
                () -> assertThat(sqlLiteralTimestampUtc.getValue(), equalTo("2015-12-01 12:01:01.1234")));
    }

    @Test
    void testParseLiteralDouble() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_double\", " //
                + "   \"value\" : \"1.234\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralDouble.getType(), equalTo(LITERAL_DOUBLE)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(1.234)));
    }

    @Test
    void testParseLiteralExactNumeric() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_exactnumeric\", " //
                + "   \"value\" : \"12345\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralExactnumeric sqlLiteralExactnumeric = (SqlLiteralExactnumeric) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        assertAll(() -> assertThat(sqlLiteralExactnumeric.getType(), equalTo(LITERAL_EXACTNUMERIC)),
                () -> assertThat(sqlLiteralExactnumeric.getValue(), equalTo(new BigDecimal(12345))));
    }

    @Test
    void testParseLiteralString() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_string\", " //
                + "   \"value\" : \"my string\" " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlLiteralString sqlLiteralString = (SqlLiteralString) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateAnd sqlPredicateAnd = (SqlPredicateAnd) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final List<SqlNode> expressions = sqlPredicateAnd.getAndedPredicates();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) expressions.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) expressions.get(1);
        assertAll(() -> assertThat(sqlPredicateAnd.getType(), equalTo(PREDICATE_AND)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo(1.0)), //
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo(2.0)));
    }

    @Test
    void testParsePredicateAndEmptyExpressions() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_and\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateAnd sqlPredicateAnd = (SqlPredicateAnd) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateOr sqlPredicateAnd = (SqlPredicateOr) this.pushdownSqlParser.parseExpression(this.jsonObject);
        final List<SqlNode> expressions = sqlPredicateAnd.getOrPredicates();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) expressions.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) expressions.get(1);
        assertAll(() -> assertThat(sqlPredicateAnd.getType(), equalTo(PREDICATE_OR)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo(1.0)), //
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo(2.0)));
    }

    @Test
    void testParsePredicateOrEmptyExpressions() {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_or\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateOr sqlPredicateOr = (SqlPredicateOr) this.pushdownSqlParser.parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateNot sqlPredicateNot = (SqlPredicateNot) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateEqual sqlPredicateEqual = (SqlPredicateEqual) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateEqual.getType(), equalTo(PREDICATE_EQUAL)),
                () -> assertThat(left.getValue(), equalTo(1.234)), //
                () -> assertThat(right.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateNotEqual sqlPredicateNotEqual = (SqlPredicateNotEqual) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateNotEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateNotEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateNotEqual.getType(), equalTo(PREDICATE_NOTEQUAL)),
                () -> assertThat(left.getValue(), equalTo(1.2)), //
                () -> assertThat(right.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateLess sqlPredicateLess = (SqlPredicateLess) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLess.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLess.getRight();
        assertAll(() -> assertThat(sqlPredicateLess.getType(), equalTo(PREDICATE_LESS)),
                () -> assertThat(left.getValue(), equalTo(1.2)), //
                () -> assertThat(right.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateLessEqual sqlPredicateLessEqual = (SqlPredicateLessEqual) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLessEqual.getLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLessEqual.getRight();
        assertAll(() -> assertThat(sqlPredicateLessEqual.getType(), equalTo(PREDICATE_LESSEQUAL)),
                () -> assertThat(left.getValue(), equalTo(1.2)), //
                () -> assertThat(right.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateLike sqlPredicateLike = (SqlPredicateLike) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateLikeRegexp sqlPredicateLike = (SqlPredicateLikeRegexp) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateBetween sqlPredicateLike = (SqlPredicateBetween) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble left = (SqlLiteralDouble) sqlPredicateLike.getBetweenLeft();
        final SqlLiteralDouble right = (SqlLiteralDouble) sqlPredicateLike.getBetweenRight();
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateLike.getExpression();
        assertAll(() -> assertThat(sqlPredicateLike.getType(), equalTo(PREDICATE_BETWEEN)),
                () -> assertThat(left.getValue(), equalTo(1.1)), //
                () -> assertThat(expression.getValue(), equalTo(1.2)), //
                () -> assertThat(right.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateIsNull sqlPredicateIsNull = (SqlPredicateIsNull) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateIsNull.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsNull.getType(), equalTo(PREDICATE_IS_NULL)),
                () -> assertThat(expression.getValue(), equalTo(0.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateIsNotNull sqlPredicateIsNotNull = (SqlPredicateIsNotNull) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateIsNotNull.getExpression();
        assertAll(() -> assertThat(sqlPredicateIsNotNull.getType(), equalTo(PREDICATE_IS_NOT_NULL)),
                () -> assertThat(expression.getValue(), equalTo(1.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionScalar sqlFunctionScalar = (SqlFunctionScalar) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final List<SqlNode> expressions = sqlFunctionScalar.getArguments();
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) expressions.get(0);
        assertAll(() -> assertThat(sqlFunctionScalar.getType(), equalTo(FUNCTION_SCALAR)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(1.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionScalarExtract sqlFunctionScalarExtract = (SqlFunctionScalarExtract) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionScalarCast sqlFunctionScalarCast = (SqlFunctionScalarCast) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionScalarCast.getArgument();
        assertAll(() -> assertThat(sqlFunctionScalarCast.getType(), equalTo(FUNCTION_SCALAR_CAST)),
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(1.234)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateInConstList sqlPredicateInConstList = (SqlPredicateInConstList) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final List<SqlNode> arguments = sqlPredicateInConstList.getInArguments();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) arguments.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) arguments.get(1);
        final SqlLiteralDouble expression = (SqlLiteralDouble) sqlPredicateInConstList.getExpression();
        assertAll(() -> assertThat(sqlPredicateInConstList.getType(), equalTo(PREDICATE_IN_CONSTLIST)),
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo(1.0)),
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo(2.0)),
                () -> assertThat(expression.getValue(), equalTo(2.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateIsJson sqlPredicateIsJson = (SqlPredicateIsJson) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlPredicateIsNotJson sqlPredicateIsNotJson = (SqlPredicateIsNotJson) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionScalarCase sqlFunctionScalarCase = (SqlFunctionScalarCase) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final List<SqlNode> arguments = sqlFunctionScalarCase.getArguments();
        final SqlLiteralExactnumeric sqlLiteralExactnumeric1 = (SqlLiteralExactnumeric) arguments.get(0);
        final SqlLiteralExactnumeric sqlLiteralExactnumeric2 = (SqlLiteralExactnumeric) arguments.get(1);
        final List<SqlNode> results = sqlFunctionScalarCase.getResults();
        final SqlLiteralString sqlLiteralString1 = (SqlLiteralString) results.get(0);
        final SqlLiteralString sqlLiteralString2 = (SqlLiteralString) results.get(1);
        assertAll(() -> assertThat(sqlFunctionScalarCase.getType(), equalTo(FUNCTION_SCALAR_CASE)),
                () -> assertThat(sqlFunctionScalarCase.getBasis(), instanceOf(SqlNode.class)),
                () -> assertThat(sqlLiteralExactnumeric1.getValue(), equalTo(new BigDecimal(1))),
                () -> assertThat(sqlLiteralExactnumeric2.getValue(), equalTo(new BigDecimal(2))),
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionScalarJsonValue sqlFunctionScalarJsonValue = (SqlFunctionScalarJsonValue) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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
                () -> assertThat(returningDataType, equalTo(DataType.createVarChar(10000, DataType.ExaCharset.UTF8))),
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionAggregate sqlFunctionAggregate = (SqlFunctionAggregate) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final List<SqlNode> arguments = sqlFunctionAggregate.getArguments();
        final SqlLiteralDouble sqlLiteralDouble1 = (SqlLiteralDouble) arguments.get(0);
        final SqlLiteralDouble sqlLiteralDouble2 = (SqlLiteralDouble) arguments.get(1);
        assertAll(() -> assertThat(sqlFunctionAggregate.getType(), equalTo(FUNCTION_AGGREGATE)),
                () -> assertThat(sqlFunctionAggregate.getFunctionName(), equalTo("SUM")), //
                () -> assertThat(sqlLiteralDouble1.getValue(), equalTo(1.0)),
                () -> assertThat(sqlLiteralDouble2.getValue(), equalTo(2.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat = (SqlFunctionAggregateGroupConcat) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionAggregateGroupConcat.getArgument();
        assertAll(() -> assertThat(sqlFunctionAggregateGroupConcat.getType(), equalTo(FUNCTION_AGGREGATE_GROUP_CONCAT)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.getFunctionName(), equalTo("GROUP_CONCAT")), //
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(2.0)));
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionAggregateGroupConcat sqlFunctionAggregateGroupConcat = (SqlFunctionAggregateGroupConcat) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
        final SqlLiteralDouble sqlLiteralDouble = (SqlLiteralDouble) sqlFunctionAggregateGroupConcat.getArgument();
        assertAll(() -> assertThat(sqlFunctionAggregateGroupConcat.getType(), equalTo(FUNCTION_AGGREGATE_GROUP_CONCAT)),
                () -> assertThat(sqlFunctionAggregateGroupConcat.getFunctionName(), equalTo("GROUP_CONCAT")), //
                () -> assertThat(sqlLiteralDouble.getValue(), equalTo(2.0)),
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
        try (final JsonReader jsonReader = Json.createReader(new StringReader(sqlAsJson))) {
            this.jsonObject = jsonReader.readObject();
        }
        final SqlFunctionAggregateListagg listagg = (SqlFunctionAggregateListagg) this.pushdownSqlParser
                .parseExpression(this.jsonObject);
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

    @Test
    void testSimpleInnerJoinRequest() {
        final String sqlAsJson = "{" //
                + "    \"type\" : \"select\"," //
                + "    \"from\" : " //
                + "    {" //
                + "        \"type\": \"join\"," //
                + "        \"join_type\": \"inner\"," //
                + "        \"left\":" //
                + "        {" //
                + "            \"name\" : \"T1\"," //
                + "            \"type\" : \"table\"" //
                + "        }," //
                + "        \"right\":" //
                + "        {" //
                + "            \"name\" : \"T2\"," //
                + "            \"type\" : \"table\"" //
                + "        }," //
                + "        \"condition\":" //
                + "        {" //
                + "            \"left\" :" //
                + "            {" //
                + "                    \"columnNr\" : 0," //
                + "                    \"name\" : \"ID\"," //
                + "                    \"tableName\" : \"T1\"," //
                + "                    \"type\" : \"column\"" //
                + "            }," //
                + "            \"right\" :" //
                + "            {" //
                + "                    \"columnNr\" : 0," //
                + "                    \"name\" : \"ID\"," //
                + "                    \"tableName\" : \"T2\"," //
                + "                    \"type\" : \"column\"" //
                + "            }," //
                + "            \"type\" : \"predicate_equal\"" //
                + "        }" //
                + "    }" //
                + "}";

        final SqlStatementSelect select = parseSqlExpression(sqlAsJson);
        final SqlJoin from = (SqlJoin) select.getFromClause();

        assertAll(() -> assertThat(SqlNodeType.JOIN, sameInstance(from.getType())),
                () -> assertThat(JoinType.INNER, sameInstance(from.getJoinType())),
                () -> assertThat(SqlNodeType.PREDICATE_EQUAL, sameInstance(from.getCondition().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getLeft().getType())),
                () -> assertThat(SqlNodeType.TABLE, sameInstance(from.getRight().getType())));
    }

    private SqlStatementSelect parseSqlExpression(final String sqlAsJson) {
        final ByteArrayInputStream rawRequestStream = new ByteArrayInputStream(
                sqlAsJson.getBytes(StandardCharsets.UTF_8));
        final JsonReader reader = Json.createReader(rawRequestStream);
        final List<TableMetadata> tables = new ArrayList<>();
        final List<ColumnMetadata> table1Columns = new ArrayList<>();
        table1Columns.add(ColumnMetadata.builder().name("ID").adapterNotes("").type(DataType.createDecimal(18, 0))
                .nullable(true).identity(true).defaultValue("0").comment("").build());
        tables.add(new TableMetadata("T1", "", table1Columns, ""));
        final List<ColumnMetadata> table2Columns = new ArrayList<>();
        table2Columns.add(ColumnMetadata.builder().name("ID").adapterNotes("").type(DataType.createDecimal(18, 0))
                .nullable(true).identity(true).defaultValue("0").comment("").build());
        tables.add(new TableMetadata("T2", "", table2Columns, ""));
        final PushdownSqlParser parser = PushdownSqlParser.createWithTablesMetadata(tables);
        return (SqlStatementSelect) parser.parseExpression(reader.readObject());
    }
}