package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class CreateVirtualSchemaResponseTest {
    @Test
    void builder() {
        assertThat(CreateVirtualSchemaResponse.builder().build(), instanceOf(CreateVirtualSchemaResponse.class));
    }
}