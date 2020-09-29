package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.mocking.MockUtils;

class SqlColumnTest {
    private static final int ID = 1;
    private static final String TABLE_NAME = "table_name";
    private static final String METADATA_TEST_NAME = "metadata_test_name";
    private SqlColumn sqlColumn;
    private ColumnMetadata columnMetadata;

    @BeforeEach
    void setUp() {
        this.columnMetadata = ColumnMetadata.builder().name(METADATA_TEST_NAME).type(DataType.createBool()).build();
        this.sqlColumn = new SqlColumn(ID, this.columnMetadata, TABLE_NAME);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlColumn.getType(), equalTo(SqlNodeType.COLUMN));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlColumn> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlColumn)).thenReturn(this.sqlColumn);
        assertThat(this.sqlColumn.accept(visitor), equalTo(this.sqlColumn));
    }

    @Test
    void testGetId() {
        assertThat(this.sqlColumn.getId(), equalTo(ID));
    }

    @Test
    void testGetMetadata() {
        assertThat(this.sqlColumn.getMetadata(), equalTo(this.columnMetadata));
    }

    @Test
    void testGetName() {
        assertThat(this.sqlColumn.getName(), equalTo(METADATA_TEST_NAME));
    }

    @Test
    void testGetTableName() {
        assertThat(this.sqlColumn.getTableName(), equalTo(TABLE_NAME));
    }
}