package com.exasol.adapter.request.parser.json.builder;

public abstract class JsonKeyValue implements JsonEntry {

    private final String key;

    JsonKeyValue(final String key) {
        this.key = key;
    }

    public String render(final int level, final String value) {
        return String.format("\"%s\": %s", this.key, value);
    }

    static class Simple extends JsonKeyValue {
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

    static class Complex extends JsonKeyValue {
        private final JsonEntry value;

        Complex(final String key, final JsonEntry value) {
            super(key);
            this.value = value;
        }

        @Override
        public String render(final int level) {
            return render(level, this.value.render(level));
        }
    }
}
