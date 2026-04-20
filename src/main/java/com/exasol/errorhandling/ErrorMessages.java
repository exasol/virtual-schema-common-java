package com.exasol.errorhandling;

import com.exasol.errorreporting.ErrorMessageBuilder;

/**
 * This class contains constants used in error handling.
 * 
 * @deprecated This class is deprecated and will be removed in a future release. Use {@link ErrorMessageBuilder#ticketMitigation()} instead.
 */
@Deprecated(since = "18.0.0", forRemoval = true)
public final class ErrorMessages {
    private static final String FILE_A_BUG_REPORT_MSG = "Please file a bug report quoting this message.";

    private ErrorMessages() {
        // prevent instantiation
    }

    /**
     * Create a message that asks the user to file a bug report
     *
     * @return error message
     * @deprecated use {@link ErrorMessageBuilder#ticketMitigation()} instead
     */
    @Deprecated(since = "18.0.0", forRemoval = true)
    public static String askForBugReport() {
        return FILE_A_BUG_REPORT_MSG;
    }
}
