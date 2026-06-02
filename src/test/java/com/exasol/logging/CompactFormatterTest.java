package com.exasol.logging;

import static com.exasol.logging.RemoteLogManagerTest.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompactFormatterTest {
    private LogRecord record;
    private final CompactFormatter formatter = new CompactFormatter();

    @BeforeEach
    void beforeEach() {
        this.record = new LogRecord(Level.SEVERE, "message");
    }

    @Test
    void testFormat() {
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +message"));
    }

    @Test
    void testFormatUsesUtcTimestamp() {
        this.record.setMillis(0);
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, startsWith("1970-01-01 00:00:00.000 SEVERE"));
    }

    @ParameterizedTest
    @MethodSource("classNameFormats")
    void testFormatWithClassName(final String sourceClassName, final String expectedContent) {
        this.record.setSourceClassName(sourceClassName);
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(expectedContent));
    }

    private static Stream<Arguments> classNameFormats() {
        return Stream.of(Arguments.of("", " SEVERE +message"),
                Arguments.of("example", " SEVERE +\\[example\\] +message"),
                Arguments.of("com.exasol.example", " SEVERE +\\[c\\.e\\.example\\] +message"),
                Arguments.of("com.exasol..example", " SEVERE +\\[c\\.e\\.\\.example\\] +message"),
                Arguments.of("com.exasol.", " SEVERE +\\[c\\.e\\.\\] +message"),
                Arguments.of(".", " SEVERE +\\[\\.\\] +message"));
    }

    @Test
    void testFormatWithPlaceholders() {
        final LogRecord recordWithPlaceholders = new LogRecord(Level.SEVERE, "message {0} : {1}");
        recordWithPlaceholders.setParameters(new String[] { "foo", "bar" });
        final String formattedRecord = this.formatter.format(recordWithPlaceholders);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +message foo : bar"));
    }

    @Test
    void testFormatException() {
        final IllegalStateException cause = new IllegalStateException("the cause");
        final IllegalArgumentException exception = new IllegalArgumentException("the exception", cause);
        this.record.setMessage("the message");
        this.record.setThrown(exception);
        final String formattedRecord = this.formatter.format(this.record);
        assertAll(
                () -> assertThat(formattedRecord,
                        matchesPattern(TIMESTAMP_PATTERN + " SEVERE  the message\\n(.*" + LINEFEED_PATTERN + ")*")),
                () -> assertThat(formattedRecord, containsString("the exception")),
                () -> assertThat(formattedRecord, containsString("the cause")));
    }
}
