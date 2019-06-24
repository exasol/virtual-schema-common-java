package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.converter.ResponseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RefreshResponseTest {
    @Mock
    SchemaMetadata schemaMetadata;

    @Test
    void builder() {
        assertThat(RefreshResponse.builder().schemaMetadata(this.schemaMetadata).build(),
                instanceOf(RefreshResponse.class));
    }

    @Test
    void testRefreshResponseThrowsException() {
        assertThrows(ResponseException.class, RefreshResponse.builder()::build);
    }
}