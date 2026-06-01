package com.exasol.adapter.request;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

class RefreshRequestTest {
    private SchemaMetadataInfo schemaMetadataInfo;
    private RefreshRequest refreshRequest;
    private List<String> tables;

    @BeforeEach
    void setUp() {
        this.tables = new ArrayList<>();
        this.tables.add("TEST_TABLE");
        this.refreshRequest = new RefreshRequest(this.schemaMetadataInfo, this.tables);
    }

    @Test
    void testCreateWithEmptyTablesThrowsException() {
        final List<String> emptyList = Collections.emptyList();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RefreshRequest(this.schemaMetadataInfo, emptyList));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-32"));
    }

    @Test
    void testGetTables() {
        assertThat(this.refreshRequest.getTables(), equalTo(this.tables));
    }

    @Test
    void testCopiesSelectedTablesDefensively() {
        this.tables.add("MUTATED");
        assertThat(this.refreshRequest.getTables(), equalTo(List.of("TEST_TABLE")));
    }

    @Test
    void testGetTablesReturnsUnmodifiableList() {
        final List<String> selectedTables = this.refreshRequest.getTables();
        assertThrows(UnsupportedOperationException.class, () -> selectedTables.add("MUTATED"));
    }

    @Test
    void testIsRefreshForTablesTrue() {
        assertTrue(this.refreshRequest.refreshesOnlySelectedTables());
    }

    @Test
    void testIsRefreshForTablesFalse() {
        this.refreshRequest = new RefreshRequest(this.schemaMetadataInfo);
        assertFalse(this.refreshRequest.refreshesOnlySelectedTables());
        assertThat(this.refreshRequest.getTables(), empty());
    }
}
