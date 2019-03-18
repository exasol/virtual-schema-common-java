package com.exasol.adapter.request.parser;

import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.request.parser.PushdownSqlParser;
import com.exasol.adapter.sql.*;

class PushDownSqlParserTest {
    @Test
    void testSimpleInnerJoinRequest() throws Exception {
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

        assertSame(SqlNodeType.JOIN, from.getType());
        assertSame(JoinType.INNER, from.getJoinType());
        assertSame(SqlNodeType.PREDICATE_EQUAL, from.getCondition().getType());
        assertSame(SqlNodeType.TABLE, from.getLeft().getType());
        assertSame(SqlNodeType.TABLE, from.getRight().getType());
    }

    private SqlStatementSelect parseSqlExpression(final String sqlAsJson) {
        final ByteArrayInputStream rawRequestStream = new ByteArrayInputStream(
                sqlAsJson.getBytes(StandardCharsets.UTF_8));
        final JsonReader reader = Json.createReader(rawRequestStream);
        final PushdownSqlParser parser = PushdownSqlParser.create(null);
        final SqlStatementSelect select = (SqlStatementSelect) parser.parseExpression(reader.readObject());
        return select;
    }
}