package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlUtilsTest {
    private static final String TABLE_WITHOUT_QUOTES = "TABLE";
    private static final String TABLE_WITH_QUOTES = "_TaBlE_";
    private static final String TABLE_WITH_QUOTES_2 = "\"table\"";
    private Map<String, String> map;

    @BeforeEach
    public void setUp() {
        this.map = new HashMap<>();
    }

    @Test
    void testQuoteIdentifierIfNeededWithoutQuotes() {
        assertThat(SqlUtils.quoteIdentifierIfNeeded("TABLE", Collections.emptyMap()),
              equalTo(TABLE_WITHOUT_QUOTES));
    }

    @Test
    void testQuoteIdentifierIfNeededWithQuotesUnderscore() {
        this.map.put("QUOTE_CHAR", "_");
        assertThat(SqlUtils.quoteIdentifierIfNeeded("TaBlE", this.map), equalTo(TABLE_WITH_QUOTES));
    }

    @Test
    void testQuoteIdentifierIfNeededWithQuotesSlash() {
        this.map.put("QUOTE_CHAR", "\"");
        assertThat(SqlUtils.quoteIdentifierIfNeeded("table", this.map),
              equalTo(TABLE_WITH_QUOTES_2));
    }
}