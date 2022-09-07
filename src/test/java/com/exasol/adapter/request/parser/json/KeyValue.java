package com.exasol.adapter.request.parser.json;

public abstract class KeyValue implements JsonValue {

    public static KeyValue entry(final String key, final String value) {
        return new Simple(key, value, "\"");
    }

    public static KeyValue entry(final String key, final int value) {
        return new Simple(key, value);
    }

    public static KeyValue entry(final String key, final boolean value) {
        return new Simple(key, value);
    }

    public static KeyValue entry(final String key, final JsonBuilder value) {
        return new Complex(key, value);
    }

    private final String key;

    KeyValue(final String key) {
        this.key = key;
    }

    public String render(final int level, final String value) {
        return String.format("\"%s\": %s", this.key, value);
    }

    static class Simple extends KeyValue {
        protected final Object value;
        final String quote;

        Simple(final String key, final Object value) {
            this(key, value, "");
        }

        Simple(final String key, final Object value, final String quote) {
            super(key);
            this.value = value;
            this.quote = quote;
        }

        @Override
        public String render(final int level) {
            return render(level, this.quote + String.valueOf(this.value) + this.quote);
        }
    }

    static class Complex extends KeyValue {
        private final JsonValue value;

        Complex(final String key, final JsonValue value) {
            super(key);
            this.value = value;
        }

        @Override
        public String render(final int level) {
            return render(level, this.value.render(level));
        }
    }
}
