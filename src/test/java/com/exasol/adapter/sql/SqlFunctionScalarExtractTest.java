package com.exasol.adapter.sql;

import static com.exasol.adapter.sql.SqlFunctionScalarExtract.ExtractParameter.SECOND;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlFunctionScalarExtractTest {
    private SqlFunctionScalarExtract sqlFunctionScalarExtract;
    private SqlNode argument;

    @BeforeEach
    void setUp() {
        this.argument = new SqlLiteralTimestamp("2019-02-12 12:07:00");
        this.sqlFunctionScalarExtract = new SqlFunctionScalarExtract(SECOND, this.argument);
    }

    @Test
    void getToExtract() {
        assertThat(this.sqlFunctionScalarExtract.getToExtract(), equalTo("SECOND"));
    }

    @Test
    void testGetArguments() {
        assertThat(this.sqlFunctionScalarExtract.getArgument(), equalTo(this.argument));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlFunctionScalarExtract.getType(), equalTo(SqlNodeType.FUNCTION_SCALAR_EXTRACT));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlFunctionScalarExtract> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlFunctionScalarExtract)).thenReturn(this.sqlFunctionScalarExtract);
        assertThat(this.sqlFunctionScalarExtract.accept(visitor), equalTo(this.sqlFunctionScalarExtract));
    }
}