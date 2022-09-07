package com.exasol.adapter.request.parser.json;

public interface JsonValue {
    String render(int indent);

    default String render() {
        return render(0);
    }
}