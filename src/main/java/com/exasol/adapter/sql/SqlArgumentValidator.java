package com.exasol.adapter.sql;

import java.util.List;

public final class SqlArgumentValidator {
    private SqlArgumentValidator() {
        //Intentionally left blank
    }

    public static void validateSqlFunctionArguments(final List<SqlNode> arguments, final Class usedClass) {
        if (arguments == null) {
            throw new IllegalArgumentException(usedClass.getName() + " constructor expects an argument." + "But the argument is NULL.");
        }
        if (arguments.size() != 1) {
            throw new IllegalArgumentException(usedClass.getName() + " constructor expects exactly one argument." + "But got " + arguments.size() + "arguments.");
        }
        if (arguments.get(0) == null) {
            throw new IllegalArgumentException(usedClass.getName() + " constructor expects an argument." + "But the argument is NULL.");
        }
    }
}
