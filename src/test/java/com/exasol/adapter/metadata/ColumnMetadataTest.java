package com.exasol.adapter.metadata;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ColumnMetadataTest {
    private static final DataType TYPE_DOUBLE = DataType.createDouble();
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private ColumnMetadata.Builder builder;

    @BeforeEach
    void setUp() {
        this.builder = ColumnMetadata.builder().name(COLUMN_NAME).type(TYPE_DOUBLE);
    }

    @Test
    void testDefaultValues() {
        final ColumnMetadata metadata = ColumnMetadata.builder().name("FOO").type(DataType.createBool()).build();
        assertAll(() -> assertThat("adapter notes", metadata.getAdapterNotes(), equalTo("")),
                () -> assertThat("nullable", metadata.isNullable(), equalTo(true)),
                () -> assertThat("identity", metadata.isIdentity(), equalTo(false)),
                () -> assertThat("default value", metadata.getDefaultValue(), equalTo(null)),
                () -> assertThat("comment", metadata.getComment(), equalTo("")));
    }

    @Test
    void testBuildWithoutNameThrowsException() {
        final ColumnMetadata.Builder builder = ColumnMetadata.builder().type(DataType.createBool());
        final IllegalStateException exception = assertThrows(IllegalStateException.class, builder::build);
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-5"));
    }

    @Test
    void testBuildWithEmptyNameThrowsException() {
        final ColumnMetadata.Builder builder = ColumnMetadata.builder().name("").type(DataType.createBool());
        final IllegalStateException exception = assertThrows(IllegalStateException.class, builder::build);
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-5"));
    }

    @Test
    void testBuildWithoutTypeThrowsException() {
        final ColumnMetadata.Builder builder = ColumnMetadata.builder().name("The_column_name");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, builder::build);
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-6"));
    }

    @Test
    void testGetName() {
        assertThat(this.builder.build().getName(), equalTo(COLUMN_NAME));
    }

    @Test
    void testGetType() {
        assertThat(this.builder.build().getType(), equalTo(TYPE_DOUBLE));
    }

    @Test
    void testGetAdapterNotes() {
        assertThat(this.builder.adapterNotes("notes").build().getAdapterNotes(), equalTo("notes"));
    }

    @Test
    void testIsNullableFalse() {
        assertThat(this.builder.nullable(false).build().isNullable(), equalTo(false));
    }

    @Test
    void testIsIdentityTrue() {
        assertThat(this.builder.identity(true).build().isIdentity(), equalTo(true));
    }

    @Test
    void testHasDefaultTrue() {
        assertThat(this.builder.defaultValue("default").build().hasDefault(), equalTo(true));
    }

    @Test
    void testHasDefaultFalse() {
        assertThat(this.builder.build().hasDefault(), equalTo(false));
    }

    @Test
    void testGetDefaultValue() {
        assertThat(this.builder.defaultValue("another default").build().getDefaultValue(), equalTo("another default"));
    }

    @Test
    void testHasCommentTrue() {
        assertThat(this.builder.comment("comment").build().hasComment(), equalTo(true));
    }

    @Test
    void testHasCommentFalse() {
        assertThat(this.builder.build().hasComment(), equalTo(false));
    }

    @Test
    void testGetComment() {
        assertThat(this.builder.comment("another comment").build().getComment(), equalTo("another comment"));
    }

    @Test
    void testGetOriginalTypeName() {
        assertThat(this.builder.originalTypeName("original type").build().getOriginalTypeName(),
                equalTo("original type"));
    }

    @Test
    void assertToString() {
        assertThat(this.builder.build().toString(), equalTo(
                "ColumnMetadata{name=\"COLUMN_NAME\", adapterNotes=\"\", type=DOUBLE, isNullable=true, isIdentity=false}"));

    }

    @Test
    void assertToStringWithOptionalFields() {
        assertThat(this.builder.adapterNotes("notes").defaultValue("default").comment("comment").build().toString(),
                equalTo("ColumnMetadata{name=\"COLUMN_NAME\", adapterNotes=\"notes\", type=DOUBLE, isNullable=true, isIdentity=false, defaultValue=\"default\", comment=\"comment\"}"));

    }

    @Test
    void testEqualsAndHashContract() {
        EqualsVerifier.forClass(ColumnMetadata.class).verify();
    }
}