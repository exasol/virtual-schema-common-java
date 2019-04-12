package com.exasol.adapter.metadata;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.DataType.ExaCharset;

class TableMetadataTest {
    @Test
    void testDescribe() {
        final String name = "FooBar";
        final List<ColumnMetadata> columns = new ArrayList<>();
        columns.add(ColumnMetadata.builder().name("C1").type(DataType.createBool()).build());
        columns.add(ColumnMetadata.builder().name("C2").type(DataType.createVarChar(70, ExaCharset.ASCII)).build());
        final TableMetadata table = new TableMetadata(name, "", columns, "");
        assertThat(table.describe(), equalTo("FooBar (C1 BOOLEAN, C2 VARCHAR(70) ASCII)"));
    }
}