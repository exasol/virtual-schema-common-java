package com.exasol.adapter.metadata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.DataType.ExaCharset;

class TableMetadataTest {
    private TableMetadata tableMetadata;

    @BeforeEach
    void setUp() {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(ColumnMetadata.builder().name("C1").type(DataType.createBool()).build());
        columns.add(ColumnMetadata.builder().name("C2").type(DataType.createVarChar(70, ExaCharset.ASCII)).build());
        final String name = "FooBar";
        this.tableMetadata = new TableMetadata(name, "", columns, "");
    }

    @Test
    void testDescribe() {
        assertThat(this.tableMetadata.describe(), equalTo("FooBar (C1 BOOLEAN, C2 VARCHAR(70) ASCII)"));
    }

    @Test
    void testToString() {
        assertThat(this.tableMetadata.toString(),
                equalTo("TableMetadata{name=FooBar, adapterNotes=, "
                        + "columns=[ColumnMetadata{name=\"C1\", adapterNotes=\"\", type=BOOLEAN, "
                        + "isNullable=true, isIdentity=false}, ColumnMetadata{name=\"C2\", adapterNotes=\"\", "
                        + "type=VARCHAR(70) ASCII, isNullable=true, isIdentity=false}], comment=}\n"));
    }

    @Test
    void testCopiesColumnsDefensively() {
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(ColumnMetadata.builder().name("C1").type(DataType.createBool()).build());
        final TableMetadata metadata = new TableMetadata("FooBar", "", columns, "");
        columns.add(ColumnMetadata.builder().name("C2").type(DataType.createBool()).build());
        assertThat(metadata.getColumns().size(), equalTo(1));
    }

    @Test
    void testGetColumnsReturnsUnmodifiableList() {
        final List<ColumnMetadata> columns = this.tableMetadata.getColumns();
        final ColumnMetadata metadata = ColumnMetadata.builder().name("C3").type(DataType.createBool()).build();
        assertThrows(UnsupportedOperationException.class, () -> columns.add(metadata));
    }

    @Test
    void testTreatsNullColumnsAsEmptyList() {
        assertThat(new TableMetadata("FooBar", "", null, "").getColumns(), equalTo(Collections.emptyList()));
    }
}
