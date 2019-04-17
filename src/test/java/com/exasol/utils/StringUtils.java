package com.exasol.utils;

public final class StringUtils {
    private StringUtils() {
        // prevent instanciation
    }

    // Original solution provided by user 'leorleor' on Stackoverflow
    // https://stackoverflow.com/questions/1143951/what-is-the-simplest-way-to-convert-a-java-string-from-all-caps-words-separated
    public static String toCamelCase(final String value) {
        final StringBuilder builder = new StringBuilder();
        final char delimiter = '_';
        boolean lower = false;
        for (int charInd = 0; charInd < value.length(); ++charInd) {
            final char valueChar = value.charAt(charInd);
            if (valueChar == delimiter) {
                lower = false;
            } else if (lower) {
                builder.append(Character.toLowerCase(valueChar));
            } else {
                builder.append(Character.toUpperCase(valueChar));
                lower = true;
            }
        }
        return builder.toString();
    }
}