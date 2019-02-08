package com.exasol.adapter.sql;

import com.exasol.adapter.metadata.TableMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

class SqlTableTest {
    private static final String VALUE = "2019-02-07";
    private static final String TEST_NAME = "test name";
    private static final String TEST_ALIAS = "test alias";
    private SqlTable sqlTable;
    private TableMetadata tableMetadata;

    @BeforeEach
    void setUp() {
        sqlTable = new SqlTable(TEST_NAME, TEST_ALIAS, tableMetadata);
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlTable.toSimpleSql(), equalTo("\"" + TEST_NAME + "\""));
    }

    @Test
    void testGetType() {
        assertThat(sqlTable.getType(), equalTo(SqlNodeType.TABLE));
    }

    @Test
    void testHasAliasTrue() {
        assertTrue(sqlTable.hasAlias());
    }

    @Test
    void testHasAliasFalse() {
        sqlTable = new SqlTable(TEST_NAME, TEST_NAME, tableMetadata);
        assertFalse(sqlTable.hasAlias());
    }

    @Test
    void testGetName() {
        assertThat(sqlTable.getName(), equalTo(TEST_NAME));
    }

    @Test
    void testGetAlias() {
        assertThat(sqlTable.getAlias(), equalTo(TEST_ALIAS));
    }

    @Test
    void testGetMetadata() {
        assertThat(sqlTable.getMetadata(), equalTo(tableMetadata));
    }
}