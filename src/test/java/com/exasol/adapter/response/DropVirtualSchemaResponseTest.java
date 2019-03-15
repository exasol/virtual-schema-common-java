package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class DropVirtualSchemaResponseTest {
    @Test
    void builder() {
        assertThat(DropVirtualSchemaResponse.builder().build(), instanceOf(DropVirtualSchemaResponse.class));
    }
}