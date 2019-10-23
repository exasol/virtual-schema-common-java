package com.exasol.adapter.sql;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.DataType.ExaCharset;

class SqlNodeTest {
    @Test
    void testToSimpleSql() {
        final SqlNode node = getTestSqlNode();
        final String expectedSql = "SELECT \"USER_ID\", COUNT(\"URL\") FROM \"CLICKS\"" + " WHERE 1 < \"USER_ID\"" //
                + " GROUP BY \"USER_ID\"" + " HAVING 1 < COUNT(\"URL\")" + " ORDER BY \"USER_ID\"" + " LIMIT 10";
        final String actualSql = node.toSimpleSql();
        assertEquals(expectedSql, actualSql);
    }

    private SqlNode getTestSqlNode() {
        final TableMetadata clicksMeta = getClicksTableMetadata();
        final SqlTable fromClause = new SqlTable("CLICKS", clicksMeta);
        final SqlSelectList selectList = SqlSelectList.createRegularSelectList(
                List.of(new SqlColumn(0, clicksMeta.getColumns().get(0)), new SqlFunctionAggregate(
                        AggregateFunction.COUNT, List.of(new SqlColumn(1, clicksMeta.getColumns().get(1))), false)));
        final SqlNode whereClause = new SqlPredicateLess(new SqlLiteralExactnumeric(BigDecimal.ONE),
                new SqlColumn(0, clicksMeta.getColumns().get(0)));
        final SqlExpressionList groupBy = new SqlGroupBy(List.of(new SqlColumn(0, clicksMeta.getColumns().get(0))));
        final SqlNode countUrl = new SqlFunctionAggregate(AggregateFunction.COUNT,
                List.of(new SqlColumn(1, clicksMeta.getColumns().get(1))), false);
        final SqlNode having = new SqlPredicateLess(new SqlLiteralExactnumeric(BigDecimal.ONE), countUrl);
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(new SqlColumn(0, clicksMeta.getColumns().get(0))),
                List.of(true), List.of(true));
        final SqlLimit limit = new SqlLimit(10);
        return SqlStatementSelect.builder().selectList(selectList).fromClause(fromClause).whereClause(whereClause)
                .groupBy(groupBy).having(having).orderBy(orderBy).limit(limit).build();
    }

    private TableMetadata getClicksTableMetadata() {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(ColumnMetadata.builder().name("USER_ID").adapterNotes("").type(DataType.createDecimal(18, 0))
                .nullable(true).identity(false).defaultValue("").comment("").build());
        columns.add(ColumnMetadata.builder().name("URL").adapterNotes("")
                .type(DataType.createVarChar(10000, ExaCharset.UTF8)).nullable(true).identity(false).defaultValue("")
                .comment("").build());
        return new TableMetadata("CLICKS", "", columns, "");
    }
}