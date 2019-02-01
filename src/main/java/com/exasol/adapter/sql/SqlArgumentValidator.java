package com.exasol.adapter.sql;

import java.util.List;

public final class SqlArgumentValidator {
    private SqlArgumentValidator() {
        //Intentionally left blank
    }

    public static void validateSqlFunctionArguments(List<SqlNode> arguments, String className) {
        if (arguments == null) {
            throw new IllegalArgumentException(className + " constructor expects an argument." + "But the argument is NULL.");
        }
        if (arguments.size() != 1) {
            throw new IllegalArgumentException(className + " constructor expects exactly one argument." + "But got " + arguments.size() + "arguments.");
        }
        if (arguments.get(0) == null) {
            throw new IllegalArgumentException(className + " constructor expects an argument." + "But the argument is NULL.");
        }
    }
}
