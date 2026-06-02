package com.exasol.logging;

import static com.exasol.logging.RemoteLogManagerTest.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompactFormatterTest {
    private LogRecord logRecord;
    private final CompactFormatter formatter = new CompactFormatter();

    @BeforeEach
    void beforeEach() {
        this.logRecord = new LogRecord(Level.SEVERE, "message");
    }

    @Test
    void testFormat() {
        final String formattedRecord = this.formatter.format(this.logRecord);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +message"));
    }

    @Test
    void testFormatUsesUtcTimestamp() {
        this.logRecord.setInstant(Instant.ofEpochMilli(0));
        final String formattedRecord = this.formatter.format(this.logRecord);
        assertThat(formattedRecord, equalTo("1970-01-01 00:00:00.000 SEVERE  message\n"));
    }

    @ParameterizedTest
    @MethodSource("classNameFormats")
    void testFormatWithClassName(final String sourceClassName, final String expectedContent) {
        this.logRecord.setSourceClassName(sourceClassName);
        final String formattedRecord = this.formatter.format(this.logRecord);
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
        this.logRecord.setMessage("the message");
        this.logRecord.setThrown(exception);
        final String formattedRecord = this.formatter.format(this.logRecord);
        assertAll(
                () -> assertThat(formattedRecord,
                        matchesPattern(TIMESTAMP_PATTERN + " SEVERE  the message\\n(.*" + LINEFEED_PATTERN + ")*")),
                () -> assertThat(formattedRecord, containsString("the exception")),
                () -> assertThat(formattedRecord, containsString("the cause")));
    }
}
