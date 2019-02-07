package com.exasol.adapter.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class ColumnMetadataTest {
    private static final String TEST_NAME = "testName";
    private static final String TEST_ADAPTER_NOTES = "testAdapterNotes";
    private static final boolean NULLABLE = true;
    private static final boolean IS_IDENTITY = true;
    private static final String TEST_DEFAULT_VALUE = "testDefaultValue";
    private static final String TEST_COMMENT = "testComment";
    private ColumnMetadata columnMetadata;
    private final DataType dataType = DataType.createDouble();

    @BeforeEach
    void SetUp() {
        columnMetadata = new ColumnMetadata(TEST_NAME, TEST_ADAPTER_NOTES, dataType, NULLABLE, IS_IDENTITY, TEST_DEFAULT_VALUE, TEST_COMMENT);
    }

    @Test
    void testGetName() {
        assertThat(columnMetadata.getName(), equalTo(TEST_NAME));
    }

    @Test
    void testGetAdapterNotes() {
        assertThat(columnMetadata.getAdapterNotes(), equalTo(TEST_ADAPTER_NOTES));
    }

    @Test
    void testGetType() {
        assertThat(columnMetadata.getType(), equalTo(dataType));
    }

    @Test
    void testIsNullable() {
        assertThat(columnMetadata.isNullable(), equalTo(NULLABLE));
    }

    @Test
    void testIsIdentity() {
        assertThat(columnMetadata.isIdentity(), equalTo(IS_IDENTITY));
    }

    @Test
    void testHasDefault() {
        assertThat(columnMetadata.hasDefault(), equalTo(true));
    }

    @Test
    void testGetDefaultValue() {
        assertThat(columnMetadata.getDefaultValue(), equalTo(TEST_DEFAULT_VALUE));
    }

    @Test
    void testHasComment() {
        assertThat(columnMetadata.hasComment(), equalTo(true));
    }

    @Test
    void testGetComment() {
        assertThat(columnMetadata.getComment(), equalTo(TEST_COMMENT));
    }

    @Test
    void testHasNoDefaultValueAndComment() {
        final ColumnMetadata columnMetadata2 = new ColumnMetadata(TEST_NAME, TEST_ADAPTER_NOTES, dataType, NULLABLE, IS_IDENTITY, "", "");
        assertThat(columnMetadata2.hasDefault(), equalTo(false));
        assertThat(columnMetadata2.hasComment(), equalTo(false));
    }
}