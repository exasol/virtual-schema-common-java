package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.ColumnMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SqlColumnTest {
    private static final int ID = 1;
    private static final String TABLE_NAME = "table_name";
    private static final String METADATA_TEST_NAME = "metadata_test_name";
    private SqlColumn sqlColumn;
    @Mock private ColumnMetadata columnMetadata;

    @BeforeEach
    void setUp() {
        when(this.columnMetadata.getName()).thenReturn(METADATA_TEST_NAME);
        this.sqlColumn = new SqlColumn(ID, this.columnMetadata, TABLE_NAME);
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlColumn.toSimpleSql(), equalTo("\"" + METADATA_TEST_NAME + "\""));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlColumn.getType(), equalTo(SqlNodeType.COLUMN));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlColumn)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlColumn.accept(visitor), equalTo(sqlLiteralNull));
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