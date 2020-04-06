package com.exasol.adapter.sql;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlArgumentValidatorTest {
    @Test
    void testValidateSqlFunctionArgumentsNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> SqlArgumentValidator.validateSingleAgrumentFunctionParameter(null, SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionArgumentsEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SqlArgumentValidator
                .validateSingleAgrumentFunctionParameter(Collections.emptyList(), SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionArgumentsWithJsonNodeSetToNullThrowsException() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionScalar.class));
    }

    @Test
    void testValidateSqlFunctionNoException() {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(new SqlLiteralNull());
        SqlArgumentValidator.validateSingleAgrumentFunctionParameter(arguments, SqlFunctionScalar.class);
    }
}