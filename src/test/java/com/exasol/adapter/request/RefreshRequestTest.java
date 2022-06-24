package com.exasol.adapter.request;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

@ExtendWith(MockitoExtension.class)
class RefreshRequestTest {
    @Mock
    private SchemaMetadataInfo schemaMetadataInfo;
    private RefreshRequest refreshRequest;
    private List<String> tables;

    @BeforeEach
    void setUp() throws AdapterException {
        this.tables = new ArrayList<>();
        this.tables.add("TEST_TABLE");
        this.refreshRequest = new RefreshRequest(this.schemaMetadataInfo, this.tables);
    }

    @Test
    void testCreateWithEmptyTablesThrowsException() {
        final List<String> tables = Collections.emptyList();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RefreshRequest(this.schemaMetadataInfo, tables));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-32"));
    }

    @Test
    void testGetTables() {
        assertThat(this.refreshRequest.getTables(), equalTo(this.tables));
    }

    @Test
    void testIsRefreshForTablesTrue() {
        assertTrue(this.refreshRequest.refreshesOnlySelectedTables());
    }

    @Test
    void testIsRefreshForTablesFalse() {
        this.refreshRequest = new RefreshRequest(this.schemaMetadataInfo);
        assertFalse(this.refreshRequest.refreshesOnlySelectedTables());
    }
}