package com.exasol.logging;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompactFormatterTest {
    private LogRecord record;
    private final CompactFormatter formatter = new CompactFormatter();
    private final String TIMESTAMP_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}";

    @BeforeEach
    void beforeEach() {
        this.record = new LogRecord(Level.SEVERE, "message");
    }

    @Test
    void testFormat() {
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +message\\n"));
    }

    @Test
    void testFormatWithEmptyClass() {
        this.record.setSourceClassName("");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +message\\n"));
    }

    @Test
    void testFormatWithSimpleClass() {
        this.record.setSourceClassName("example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +\\[example\\] +message\\n"));
    }

    @Test
    void testFormatWithFullClass() {
        this.record.setSourceClassName("com.exasol.example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +\\[c\\.e\\.example\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstDoubleDot() {
        this.record.setSourceClassName("com.exasol..example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +\\[c\\.e\\.\\.example\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstEndDot() {
        this.record.setSourceClassName("com.exasol.");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +\\[c\\.e\\.\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstOnlyDot() {
        this.record.setSourceClassName(".");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +\\[\\.\\] +message\\n"));
    }

    @Test
    void testFormatWithPlaceholders() {
        final LogRecord recordWithPlaceholders = new LogRecord(Level.SEVERE, "message {0} : {1}");
        recordWithPlaceholders.setParameters(new String[] { "foo", "bar" });
        final String formattedRecord = this.formatter.format(recordWithPlaceholders);
        assertThat(formattedRecord, matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE +message foo : bar\\n"));
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
                        matchesPattern(this.TIMESTAMP_PATTERN + " SEVERE  the message(\\n.*)*")),
                () -> assertThat(formattedRecord, containsString("the exception")),
                () -> assertThat(formattedRecord, containsString("the cause")));
    }
}