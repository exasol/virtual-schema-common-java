package com.exasol.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatter for compact log messages.
 */
public class CompactFormatter extends Formatter {
    private static final String LOG_LEVEL_FORMAT = "%-8s";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Formats a log record according in a compact manner.
     *
     * The parts of the package name between the dots are abbreviated with their first letter. Timestamps are displayed
     * as 24h UTC+0.
     *
     * <code>yyyy-MM-dd HH:mm:ss.SSS LEVEL   [c.e.ClassName] The message.</code>
     */
    @Override
    public String format(final LogRecord record) {
        final StringBuilder builder = new StringBuilder();
        builder.append(formatTimestamp(record.getMillis()));
        builder.append(" ");
        builder.append(String.format(LOG_LEVEL_FORMAT, record.getLevel()));
        appendClassName(record.getSourceClassName(), builder);
        builder.append(formatMessage(record));
        formatException(record, builder);
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private void appendClassName(final String className, final StringBuilder builder) {
        if ((className != null) && !className.isEmpty()) {
            builder.append("[");
            appendNonEmptyClassName(className, builder);
            builder.append("] ");
        }
    }

    private void appendNonEmptyClassName(final String className, final StringBuilder builder) {
        int lastPosition = -1;
        int position = className.indexOf('.');
        while (position > 0) {
            final String characterAfterDot = className.substring(lastPosition + 1, lastPosition + 2);
            if (!(".".equals(characterAfterDot))) {
                builder.append(characterAfterDot);
            }
            builder.append(".");
            lastPosition = position;
            position = className.indexOf('.', position + 1);
        }
        builder.append(className.substring(lastPosition + 1));
    }

    private String formatTimestamp(final long millis) {
        final Instant instant = Instant.ofEpochMilli(millis);
        return this.dateTimeFormatter.format(instant.atZone(ZoneId.of("Z")));
    }

    private void formatException(final LogRecord record, final StringBuilder builder) {
        final Throwable thrown = record.getThrown();
        if (thrown != null) {
            builder.append("\n\t");
            final StringWriter errors = new StringWriter();
            thrown.printStackTrace(new PrintWriter(errors));
            builder.append(errors);
        }
    }
}