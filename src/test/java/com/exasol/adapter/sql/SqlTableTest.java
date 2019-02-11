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
        this.sqlTable = new SqlTable(TEST_NAME, TEST_ALIAS, this.tableMetadata);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlTable.toSimpleSql(), equalTo("\"" + TEST_NAME + "\""));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlTable.getType(), equalTo(SqlNodeType.TABLE));
    }

    @Test
    void testHasAliasTrue() {
        assertTrue(this.sqlTable.hasAlias());
    }

    @Test
    void testHasAliasFalse() {
        this.sqlTable = new SqlTable(TEST_NAME, TEST_NAME, this.tableMetadata);
        assertFalse(this.sqlTable.hasAlias());
    }

    @Test
    void testGetName() {
        assertThat(this.sqlTable.getName(), equalTo(TEST_NAME));
    }

    @Test
    void testGetAlias() {
        assertThat(this.sqlTable.getAlias(), equalTo(TEST_ALIAS));
    }

    @Test
    void testGetMetadata() {
        assertThat(this.sqlTable.getMetadata(), equalTo(this.tableMetadata));
    }
}