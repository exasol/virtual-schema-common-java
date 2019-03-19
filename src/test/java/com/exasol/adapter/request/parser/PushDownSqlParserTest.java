package com.exasol.adapter.request.parser;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.*;
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
        table1Columns.add(new ColumnMetadata("ID", "", DataType.createDecimal(18, 0), true, true, "0", ""));
        tables.add(new TableMetadata("T1", "", table1Columns, ""));
        final List<ColumnMetadata> table2Columns = new ArrayList<>();
        table2Columns.add(new ColumnMetadata("ID", "", DataType.createDecimal(18, 0), true, true, "0", ""));
        tables.add(new TableMetadata("T2", "", table2Columns, ""));
        final PushdownSqlParser parser = PushdownSqlParser.createWithTablesMetadata(tables);
        final SqlStatementSelect select = (SqlStatementSelect) parser.parseExpression(reader.readObject());
        return select;
    }
}