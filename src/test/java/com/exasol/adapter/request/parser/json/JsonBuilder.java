package com.exasol.adapter.request.parser.json;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JsonBuilder implements JsonValue {

    public static Group group(final JsonValue... children) {
        return new Group(children);
    }

    public static Array array(final JsonValue... children) {
        return new Array(children);
    }

    private final String prefix;
    private final String suffix;
    private final List<JsonValue> children;

    JsonBuilder(final String prefix, final String suffix, final JsonValue... children) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.children = Arrays.asList(children);
    }

    private String renderChildren(final int level) {
        return this.children.stream() //
                .map(c -> c.render(level)) //
                .collect(Collectors.joining(",\n" + indent(level), indent(level), ""));
    }

    @Override
    public String render(final int level) {
        return this.prefix + "\n" //
                + renderChildren(level + 1) + "\n" //
                + indent(level) + this.suffix;
    }

    static String indent(final int amount) {
        return (amount < 1) ? "" : repeat(" ", amount * 2);
    }

    static String repeat(final String s, final int repetitions) {
        final StringBuilder builder = new StringBuilder(repetitions);
        for (int i = 0; i < repetitions; i++) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static class Group extends JsonBuilder {
        public Group(final JsonValue... values) {
            super("{", "}", values);
        }
    }

    static class Array extends JsonBuilder {
        public Array(final JsonValue... values) {
            super("[", "]", values);
        }
    }
}
