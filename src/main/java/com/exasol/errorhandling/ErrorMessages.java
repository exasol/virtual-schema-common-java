package com.exasol.errorhandling;

/**
 * This class contains constants used in error handling.
 */
public final class ErrorMessages {
    private static final String FILE_A_BUG_REPORT_MSG = "Please file a bug report quoting this message.";

    private ErrorMessages() {
        // prevent instantiation
    }

    /**
     * Create a message that asks the user to file a bug report
     *
     * @return error message
     */
    public static String askForBugReport() {
        return FILE_A_BUG_REPORT_MSG;
    }
}
