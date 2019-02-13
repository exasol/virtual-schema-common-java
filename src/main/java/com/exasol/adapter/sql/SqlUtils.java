package com.exasol.adapter.sql;

import java.util.Map;

public final class SqlUtils {
    private SqlUtils() {
        //Intentionally left blank
    }

    @SuppressWarnings("squid:S1157")
    public static String quoteIdentifierIfNeeded(final String identifier,
          final Map<String, ?> config) {
        String quoteChar = "\"";
        if (config.containsKey("QUOTE_CHAR")) {
            quoteChar = config.get("QUOTE_CHAR").toString();
        }
        if (identifier.toUpperCase().equals(identifier)) {
            // Only upper case, no need to quote
            return identifier;
        } else {
            return quoteChar + identifier + quoteChar;
        }
    }
}
