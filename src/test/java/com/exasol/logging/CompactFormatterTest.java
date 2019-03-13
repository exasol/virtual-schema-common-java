package com.exasol.logging;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.Assert.assertThat;

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
        assertThat(formattedRecord,
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +message\\n"));
    }

    @Test
    void testFormatWithEmptyClass() {
        this.record.setSourceClassName("");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +message\\n"));
    }

    @Test
    void testFormatWithSimpleClass() {
        this.record.setSourceClassName("example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +\\[example\\] +message\\n"));
    }

    @Test
    void testFormatWithFullClass() {
        this.record.setSourceClassName("com.exasol.example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(
                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +\\[c\\.e\\.example\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstDoubleDot() {
        this.record.setSourceClassName("com.exasol..example");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord, matchesPattern(
                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +\\[c\\.e\\.\\.example\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstEndDot() {
        this.record.setSourceClassName("com.exasol.");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +\\[c\\.e\\.\\] +message\\n"));
    }

    @Test
    void testFormatRobustAgainstOnlyDot() {
        this.record.setSourceClassName(".");
        final String formattedRecord = this.formatter.format(this.record);
        assertThat(formattedRecord,
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3} SEVERE +\\[\\.\\] +message\\n"));
    }
}