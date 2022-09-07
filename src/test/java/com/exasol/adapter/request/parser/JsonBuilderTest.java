package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.json.JsonBuilder.array;
import static com.exasol.adapter.request.parser.json.JsonBuilder.group;
import static com.exasol.adapter.request.parser.json.KeyValue.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.request.parser.json.JsonBuilder;

public class JsonBuilderTest {

    @Test
    void test() {
        final JsonBuilder group = JsonBuilder.group( //
                entry("type", "pushdown"), //
                entry("pushdownRequest", group( //
                        entry("type", "select"), //
                        entry("from", group( //
                                entry("name", "FOO"), //
                                entry("type", "table") //
                        )))), //
                entry("involvedTables", array( //
                        entry("name", "FOO"), //
                        entry("columns", array(group( //
                                entry("name", "BAR"), //
                                entry("dataType", group( //
                                        entry("precision", 18), //
                                        entry("scale", 0), //
                                        entry("type", "DECIMAL") //
                                ))))))));
        System.out.println(group.render());
    }
}
