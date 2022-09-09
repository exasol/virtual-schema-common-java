package com.exasol.adapter.request.parser.json;

import java.util.*;
import java.util.stream.Collectors;

public abstract class JsonParent implements JsonEntry {

    private final String prefix;
    private final String suffix;
    private final List<JsonEntry> children;

    JsonParent(final String prefix, final String suffix, final JsonEntry... children) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.children = new ArrayList<>(Arrays.asList(children));
    }

    private String renderChildren(final int level) {
        return this.children.stream() //
                .map(c -> c.render(level)) //
                .collect(Collectors.joining(",\n" + indent(level), indent(level), ""));
    }

    public JsonParent withChild(final JsonEntry child) {
        this.children.add(child);
        return this;
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

    public static class JsonGroup extends JsonParent {
        public JsonGroup(final JsonEntry... values) {
            super("{", "}", values);
        }
    }

    public static class JsonArray extends JsonParent {
        public JsonArray(final JsonEntry... values) {
            super("[", "]", values);
        }
    }
}
