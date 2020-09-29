package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlTableTest {
    private static final String TEST_NAME = "test name";
    private static final String TEST_ALIAS = "test alias";
    private SqlTable sqlTable;

    @BeforeEach
    void setUp() {
        this.sqlTable = new SqlTable(TEST_NAME, TEST_ALIAS, null);
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
        this.sqlTable = new SqlTable(TEST_NAME, TEST_NAME, null);
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
        assertThat(this.sqlTable.getMetadata(), equalTo(null));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlTable> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlTable)).thenReturn(this.sqlTable);
        assertThat(this.sqlTable.accept(visitor), equalTo(this.sqlTable));
    }
}