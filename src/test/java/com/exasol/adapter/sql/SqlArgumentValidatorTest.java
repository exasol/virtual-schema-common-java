package com.exasol.adapter.sql;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlArgumentValidatorTest {
    @Test
    void testValidateSqlFunctionArgumentsNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SqlArgumentValidator
              .validateSqlFunctionArguments(null, SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionArgumentsEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SqlArgumentValidator
              .validateSqlFunctionArguments(Collections.emptyList(), SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionArgumentsWithJsonNodeSetToNullThrowsException() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(null);
        assertThrows(IllegalArgumentException.class, () -> SqlArgumentValidator
              .validateSqlFunctionArguments(arguments, SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionNoException() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(new SqlLiteralNull());
        SqlArgumentValidator.validateSqlFunctionArguments(arguments, SqlFunctionScalar.class);
    }
}