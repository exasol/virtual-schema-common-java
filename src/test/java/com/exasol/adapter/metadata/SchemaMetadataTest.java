package com.exasol.adapter.metadata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SchemaMetadataTest {
    @Test
    void testCopiesTablesDefensively() {
        final List<TableMetadata> tables = new ArrayList<>();
        tables.add(new TableMetadata("T1", "", List.of(), ""));
        final SchemaMetadata schemaMetadata = new SchemaMetadata("", tables);
        tables.add(new TableMetadata("T2", "", List.of(), ""));
        assertThat(schemaMetadata.getTables().size(), equalTo(1));
    }

    @Test
    void testGetTablesReturnsUnmodifiableList() {
        final SchemaMetadata schemaMetadata = new SchemaMetadata("", List.of(new TableMetadata("T1", "", List.of(), "")));
        final List<TableMetadata> tables = schemaMetadata.getTables();
        final TableMetadata table = new TableMetadata("T2", "", List.of(), "");
        assertThrows(UnsupportedOperationException.class, () -> tables.add(table));
    }

    @Test
    void testTreatsNullTablesAsEmptyList() {
        assertThat(new SchemaMetadata("", null).getTables(), empty());
    }
}
