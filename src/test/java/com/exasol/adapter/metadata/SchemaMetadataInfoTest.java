package com.exasol.adapter.metadata;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchemaMetadataInfoTest {
    private static final String TEST_NAME = "test name";
    private static final String TEST_ADAPTER_NOTES = "test adapter notes";
    private SchemaMetadataInfo schemaMetadataInfo;

    @BeforeEach
    void setUp() {
        this.schemaMetadataInfo = new SchemaMetadataInfo(TEST_NAME, TEST_ADAPTER_NOTES, Collections.emptyMap());
    }

    @Test
    void testGetSchemaName() {
        assertThat(this.schemaMetadataInfo.getSchemaName(), equalTo(TEST_NAME));
    }

    @Test
    void testGetAdapterNotes() {
        assertThat(this.schemaMetadataInfo.getAdapterNotes(), equalTo(TEST_ADAPTER_NOTES));
    }

    @Test
    void testGetProperties() {
        assertThat(this.schemaMetadataInfo.getProperties(), equalTo(Collections.emptyMap()));
    }

    @Test
    void testCopiesPropertiesDefensively() {
        final Map<String, String> properties = new HashMap<>();
        properties.put("A", "B");
        final SchemaMetadataInfo metadataInfo = new SchemaMetadataInfo(TEST_NAME, TEST_ADAPTER_NOTES, properties);
        properties.put("A", "CHANGED");
        assertThat(metadataInfo.getProperties(), hasEntry("A", "B"));
    }

    @Test
    void testGetPropertiesReturnsUnmodifiableMap() {
        assertThrows(UnsupportedOperationException.class,
                () -> this.schemaMetadataInfo.getProperties().put("A", "B"));
    }

    @Test
    void testTreatsNullPropertiesAsEmptyMap() {
        assertThat(new SchemaMetadataInfo(TEST_NAME, TEST_ADAPTER_NOTES, null).getProperties(),
                equalTo(emptyMap()));
    }

    @Test
    void testToString() {
        assertThat(this.schemaMetadataInfo.toString(),
                equalTo("SchemaMetadataInfo{schemaName=test name, adapterNotes=test adapter notes, properties={}}"));
    }
}
