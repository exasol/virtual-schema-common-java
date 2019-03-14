package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class PushDownResponseTest {
    @Test
    void builder() {
        assertThat(PushDownResponse.builder().build(), instanceOf(PushDownResponse.class));
    }
}