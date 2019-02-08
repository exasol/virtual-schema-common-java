package com.exasol.adapter.request;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RefreshRequestTest {
    @Mock
    private SchemaMetadataInfo schemaMetadataInfo;
    private RefreshRequest refreshRequest;
    private List<String> tables;

    @BeforeEach
    void SetUp() throws AdapterException {
        tables = new ArrayList<>();
        tables.add("TEST_TABLE");
        refreshRequest = new RefreshRequest(schemaMetadataInfo, tables);
    }

    @Test
    void testCreateWithEmptyTables() {
        assertThrows(AdapterException.class, () -> refreshRequest = new RefreshRequest(schemaMetadataInfo, Collections.emptyList()));
    }

    @Test
    void testGetTables() {
        assertThat(refreshRequest.getTables(), equalTo(tables));
    }

    @Test
    void testIsRefreshForTablesTrue() {
        assertTrue(refreshRequest.isRefreshForTables());
    }

    @Test
    void testIsRefreshForTablesFalse() {
        refreshRequest = new RefreshRequest(schemaMetadataInfo);
        assertFalse(refreshRequest.isRefreshForTables());
    }
}