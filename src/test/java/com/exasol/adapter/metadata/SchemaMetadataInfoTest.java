package com.exasol.adapter.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
    void testToString() {
        assertThat(this.schemaMetadataInfo.toString(),
                equalTo("SchemaMetadataInfo{schemaName=test name, adapterNotes=test adapter notes, properties={}}"));
    }
}