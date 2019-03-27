package com.exasol.adapter.request;

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
    private static final String ADAPTER_NAME = "THE_NAME";
    @Mock
    private SchemaMetadataInfo schemaMetadataInfo;
    private RefreshRequest refreshRequest;
    private List<String> tables;

    @BeforeEach
    void setUp() throws AdapterException {
        this.tables = new ArrayList<>();
        this.tables.add("TEST_TABLE");
        this.refreshRequest = new RefreshRequest(ADAPTER_NAME, this.schemaMetadataInfo, this.tables);
    }

    @Test
    void testCreateWithEmptyTablesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> this.refreshRequest = new RefreshRequest(ADAPTER_NAME,
                this.schemaMetadataInfo, Collections.emptyList()));
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
        this.refreshRequest = new RefreshRequest(ADAPTER_NAME, this.schemaMetadataInfo);
        assertFalse(this.refreshRequest.refreshesOnlySelectedTables());
    }
}