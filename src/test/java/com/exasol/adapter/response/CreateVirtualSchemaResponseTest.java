package com.exasol.adapter.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.converter.ResponseException;

@ExtendWith(MockitoExtension.class)
class CreateVirtualSchemaResponseTest {
    @Mock
    SchemaMetadata schemaMetadata;

    @Test
    void builder() {
        assertThat(CreateVirtualSchemaResponse.builder().schemaMetadata(this.schemaMetadata).build(),
                instanceOf(CreateVirtualSchemaResponse.class));
    }

    @Test
    void testCreateVirtualSchemaResponseThrowsException() {
        final ResponseException exception = assertThrows(ResponseException.class,
                CreateVirtualSchemaResponse.builder()::build);
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-21"));
    }
}