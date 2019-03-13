package com.exasol.adapter.sql;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.MetadataException;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.SqlTestUtil;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

class SqlNodeTest {
    @Test
    void testToSimpleSql() throws MetadataException {
        final SqlNode node = getTestSqlNode();
        final String expectedSql = "SELECT \"USER_ID\", COUNT(\"URL\") FROM \"CLICKS\"" +
        " WHERE 1 < \"USER_ID\"" +
        " GROUP BY \"USER_ID\"" +
        " HAVING 1 < COUNT(\"URL\")" +
        " ORDER BY \"USER_ID\"" +
        " LIMIT 10";
        final String actualSql = node.toSimpleSql();
        assertEquals(SqlTestUtil.normalizeSql(expectedSql), SqlTestUtil.normalizeSql(actualSql));
    }

    private SqlNode getTestSqlNode() throws MetadataException {
        // SELECT USER_ID, count(URL) FROM CLICKS
        // WHERE 1 < USER_ID
        // GROUP BY USER_ID
        // HAVING 1 < COUNT(URL)
        // ORDER BY USER_ID
        // LIMIT 10;
        final TableMetadata clicksMeta = getClicksTableMetadata();
        final SqlTable fromClause = new SqlTable("CLICKS", clicksMeta);
        final SqlSelectList selectList = SqlSelectList.createRegularSelectList(ImmutableList.of(
                new SqlColumn(0, clicksMeta.getColumns().get(0)),
                new SqlFunctionAggregate(AggregateFunction.COUNT, ImmutableList.<SqlNode>of(new SqlColumn(1, clicksMeta.getColumns().get(1))), false)));
        final SqlNode whereClause = new SqlPredicateLess(new SqlLiteralExactnumeric(BigDecimal.ONE), new SqlColumn(0, clicksMeta.getColumns().get(0)));
        final SqlExpressionList groupBy = new SqlGroupBy(ImmutableList.of(new SqlColumn(0, clicksMeta.getColumns().get(0))));
        final SqlNode countUrl = new SqlFunctionAggregate(AggregateFunction.COUNT, ImmutableList.of(new SqlColumn(1, clicksMeta.getColumns().get(1))), false);
        final SqlNode having = new SqlPredicateLess(new SqlLiteralExactnumeric(BigDecimal.ONE), countUrl);
        final SqlOrderBy orderBy = new SqlOrderBy(ImmutableList.of(new SqlColumn(0, clicksMeta.getColumns().get(0))), ImmutableList.of(true), ImmutableList.of(true));
        final SqlLimit limit = new SqlLimit(10);
        return new SqlStatementSelect(fromClause, selectList, whereClause, groupBy, having, orderBy, limit);
    }
    
    private TableMetadata getClicksTableMetadata() throws MetadataException {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(new ColumnMetadata("USER_ID", "", DataType.createDecimal(18, 0), true, false, "", ""));
        columns.add(new ColumnMetadata("URL", "", DataType.createVarChar(10000, ExaCharset.UTF8), true, false, "", ""));
        return new TableMetadata("CLICKS", "", columns, "");
    }

}
