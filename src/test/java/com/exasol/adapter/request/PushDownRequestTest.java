package com.exasol.adapter.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.TableMetadata;

class PushDownRequestTest {
    @Test
    void testCopiesListsDefensively() {
        final List<TableMetadata> tables = new ArrayList<>();
        tables.add(new TableMetadata("T1", "", List.of(), ""));
        final List<DataType> dataTypes = new ArrayList<>();
        dataTypes.add(DataType.createBool());
        final PushDownRequest request = new PushDownRequest(null, null, tables, dataTypes);
        tables.add(new TableMetadata("T2", "", List.of(), ""));
        dataTypes.add(DataType.createDouble());
        assertThat(request.getInvolvedTablesMetadata().size(), equalTo(1));
        assertThat(request.getSelectListDataTypes().size(), equalTo(1));
    }

    @Test
    void testGettersReturnUnmodifiableLists() {
        final PushDownRequest request = new PushDownRequest(null, null,
                List.of(new TableMetadata("T1", "", List.of(ColumnMetadata.builder().name("C1").type(DataType.createBool()).build()), "")),
                List.of(DataType.createBool()));
        assertThrows(UnsupportedOperationException.class,
                () -> request.getInvolvedTablesMetadata().add(new TableMetadata("T2", "", List.of(), "")));
        assertThrows(UnsupportedOperationException.class,
                () -> request.getSelectListDataTypes().add(DataType.createDouble()));
    }

    @Test
    void testTreatsNullListsAsEmpty() {
        final PushDownRequest request = new PushDownRequest(null, null, null, null);
        assertThat(request.getInvolvedTablesMetadata(), empty());
        assertThat(request.getSelectListDataTypes(), empty());
    }
}
