package com.exasol.adapter.request.parser.json.builder;

public class JsonValue implements JsonEntry {

    private final Object value;
    private final String quote;

    JsonValue(final Object value, final String quote) {
        this.value = value;
        this.quote = quote;
    }

    @Override
    public String render(final int indent) {
        return this.quote + String.valueOf(this.value) + this.quote;
    }
}
