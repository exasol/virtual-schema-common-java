package com.exasol.logging;

import static com.exasol.logging.RemoteLogManagerTest.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testFormatWithEmptyClass() {
        this.record.setSourceClassName("");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +message"));
    }

    @Test
    void testFormatWithSimpleClass() {
        this.record.setSourceClassName("example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +\\[example\\] +message"));
    }

    @Test
    void testFormatWithFullClass() {
        this.record.setSourceClassName("com.exasol.example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +\\[c\\.e\\.example\\] +message"));
    }

    @Test
    void testFormatRobustAgainstDoubleDot() {
        this.record.setSourceClassName("com.exasol..example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +\\[c\\.e\\.\\.example\\] +message"));
    }

    @Test
    void testFormatRobustAgainstEndDot() {
        this.record.setSourceClassName("com.exasol.");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +\\[c\\.e\\.\\] +message"));
    }

    @Test
    void testFormatRobustAgainstOnlyDot() {
        this.record.setSourceClassName(".");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesTimeStamp(" SEVERE +\\[\\.\\] +message"));
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