package com.exasol.adapter.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void setUp() {
        this.columnMetadata =
              new ColumnMetadata(TEST_NAME, TEST_ADAPTER_NOTES, this.dataType, NULLABLE,
                    IS_IDENTITY, TEST_DEFAULT_VALUE, TEST_COMMENT);
    }

    @Test
    void testGetName() {
        assertThat(this.columnMetadata.getName(), equalTo(TEST_NAME));
    }

    @Test
    void testGetAdapterNotes() {
        assertThat(this.columnMetadata.getAdapterNotes(), equalTo(TEST_ADAPTER_NOTES));
    }

    @Test
    void testGetType() {
        assertThat(this.columnMetadata.getType(), equalTo(this.dataType));
    }

    @Test
    void testIsNullable() {
        assertThat(this.columnMetadata.isNullable(), equalTo(NULLABLE));
    }

    @Test
    void testIsIdentity() {
        assertThat(this.columnMetadata.isIdentity(), equalTo(IS_IDENTITY));
    }

    @Test
    void testHasDefault() {
        assertThat(this.columnMetadata.hasDefault(), equalTo(true));
    }

    @Test
    void testGetDefaultValue() {
        assertThat(this.columnMetadata.getDefaultValue(), equalTo(TEST_DEFAULT_VALUE));
    }

    @Test
    void testHasComment() {
        assertThat(this.columnMetadata.hasComment(), equalTo(true));
    }

    @Test
    void testGetComment() {
        assertThat(this.columnMetadata.getComment(), equalTo(TEST_COMMENT));
    }

    @Test
    void testHasNoDefaultValueAndComment() {
        final ColumnMetadata columnMetadata2 =
              new ColumnMetadata(TEST_NAME, TEST_ADAPTER_NOTES, this.dataType, NULLABLE,
                    IS_IDENTITY, "", "");
        assertAll(() -> assertFalse(columnMetadata2.hasDefault()),
              () -> assertFalse(columnMetadata2.hasComment()));
    }
}