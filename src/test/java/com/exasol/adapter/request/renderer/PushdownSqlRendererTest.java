package com.exasol.adapter.request.renderer;

import static com.exasol.adapter.metadata.DataType.createDecimal;
import static com.exasol.adapter.metadata.DataType.createVarChar;
import static com.exasol.adapter.metadata.DataType.ExaCharset.UTF8;
import static com.exasol.adapter.sql.SqlNodeType.LITERAL_STRING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

import com.exasol.adapter.sql.SqlLiteralString;
import com.exasol.adapter.sql.SqlStatementSelect;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.parser.PushdownSqlParser;
import com.exasol.adapter.sql.SqlNode;

class PushdownSqlRendererTest {

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
    void testParseSelectWithGroupBy() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    private void assertParseAndRenderGeneratesSameJson(final String sqlAsJson) throws JSONException {
        assertParseAndRenderGeneratesSameJson(sqlAsJson, this.defaultParser);
    }

    private void assertParseAndRenderGeneratesSameJson(final String sqlAsJson, final PushdownSqlParser parser)
            throws JSONException {
        final JsonObject jsonObject = createJsonObjectFromString(sqlAsJson);
        final SqlNode parsed = parser.parseExpression(jsonObject);
        final String rendered = new PushdownSqlRenderer().render(parsed).toString();
        JSONAssert.assertEquals(sqlAsJson, rendered, false);
    }

    @Test
    void testParseSelectWithLimit() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseColumn() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"column\"," //
                + "   \"name\" : \"USER_ID\"," //
                + "   \"tableAlias\" : \"A\"," //
                + "   \"columnNr\" : 1, " //
                + "   \"tableName\" : \"CLICKS\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralBool() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_bool\", " //
                + "   \"value\" : true " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralNull() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_null\"" //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralDate() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_date\", " //
                + "   \"value\" : \"2015-12-01\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralIntervalDayToSeconds() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_interval\", " //
                + "   \"dataType\" : { " //
                + "        \"type\" : \"INTERVAL\", " //
                + "        \"fromTo\" :  \"DAY TO SECONDS\"" //
                + "   }, " //
                + "   \"value\" : \"2015-12-01\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralTimestamp() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamp\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralTimestamputc() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_timestamputc\", " //
                + "   \"value\" : \"2015-12-01 12:01:01.1234\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralDouble() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_double\", " //
                + "   \"value\" : \"1.234\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralExactNumeric() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_exactnumeric\", " //
                + "   \"value\" : \"100000\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseLiteralString() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"literal_string\", " //
                + "   \"value\" : \"my string\" " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateAnd() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateAndEmptyExpressions() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_and\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateOr() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateOrEmptyExpressions() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_or\", " //
                + "   \"expressions\" : [ " //
                + "   ] " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateNot() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_not\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_null\"" //
                + "   } " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateEqual() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateNotEqual() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateLess() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateLessEqual() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateLike() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateLikeRegexp() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateBetween() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateIsNull() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_null\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"0.0\" " //
                + "   } " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateIsNotNull() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_not_null\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_double\", " //
                + "        \"value\" : \"1.0\" " //
                + "   } " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionScalar() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionScalarExtract() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionScalarCast() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateInConstlist() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateIsJson() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_json\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'123'\"" //
                + "   }, " //
                + "   \"typeConstraint\" : \"VALUE\", " //
                + "   \"keyUniquenessConstraint\" : \"WITHOUT UNIQUE KEYS\"" + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParsePredicateIsNotJson() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"predicate_is_not_json\", " //
                + "   \"expression\" : { " //
                + "        \"type\" : \"literal_string\", " //
                + "        \"value\" : \"'123'\"" //
                + "   }, " //
                + "   \"typeConstraint\" : \"VALUE\", " //
                + "   \"keyUniquenessConstraint\" : \"WITHOUT UNIQUE KEYS\"" + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionScalarCase() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionScalarJsonValue() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionAggregate() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionAggregateGroupConcatNewApi() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseFunctionAggregateGroupListagg() throws JSONException {
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
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @ParameterizedTest
    @ValueSource(strings = { "pushdown_request_simple_inner_join_with_select_list.json",
            "pushdown_request_simple_inner_join_without_select_list.json",
            "pushdown_request_nested_join_with_repeated_table_without_select_list.json" })
    void testJoin(final String testFile) throws IOException, JSONException {
        final String sqlAsJson = new String(Files.readAllBytes(Path.of("src/test/resources/json/", testFile)));
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        assertParseAndRenderGeneratesSameJson(sqlAsJson, pushdownSqlParser);
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
    void testParseSelectWithoutSelectList() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CUSTOMERS\" " //
                + "   }" //
                + "}";
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithTwoTables();
        assertParseAndRenderGeneratesSameJson(sqlAsJson, pushdownSqlParser);
    }

    @Test
    void testParseSelectWithEmptySelectList() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CLICKS\" " //
                + "   }," //
                + "   \"selectList\" : []" //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testNestedJoinRequestWithoutSelectList() throws IOException, JSONException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_nested_join_without_select_list.json")));
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithThreeTables();
        assertParseAndRenderGeneratesSameJson(sqlAsJson, pushdownSqlParser);
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

    @Test
    void testNestedJoinRequestWithoutSelectListReversed() throws IOException, JSONException {
        final String sqlAsJson = new String(Files.readAllBytes(
                Paths.get("src/test/resources/json/pushdown_request_nested_join_without_select_list_reversed.json")));
        final PushdownSqlParser pushdownSqlParser = getCustomPushdownSqlParserWithThreeTables();
        assertParseAndRenderGeneratesSameJson(sqlAsJson, pushdownSqlParser);
    }

    @Test
    void testParseSelectWithSingleGroupAggregation() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"type\" : \"select\", " //
                + "   \"aggregationType\" : \"single_group\", " //
                + "    \"from\" : " //
                + "   { " //
                + "        \"type\" : \"table\", " //
                + "        \"name\" :  \"CLICKS\" " //
                + "   }, " //
                + "   \"selectList\" : [ " //
                + "   { " //
                + "        \"type\" : \"literal_null\" " //
                + "   } " //
                + "   ] " //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }

    @Test
    void testParseSelectWithSingleGroupAggregationWithAggregateFunction() throws JSONException {
        final String sqlAsJson = "{" //
                + "   \"selectList\":[" //
                + "      {" //
                + "         \"name\":\"SUM\"," //
                + "         \"arguments\":[" //
                + "            {" //
                + "               \"type\":\"literal_exactnumeric\"," //
                + "               \"value\":\"5\"" //
                + "            }" //
                + "         ]," //
                + "         \"type\":\"function_aggregate\"" //
                + "      }" //
                + "   ]," //
                + "   \"from\":{" //
                + "      \"name\":\"CLICKS\"," //
                + "      \"type\":\"table\"" //
                + "   }," //
                + "   \"type\":\"select\"" //
                + "}";
        assertParseAndRenderGeneratesSameJson(sqlAsJson);
    }
}