package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class GetCapabilitiesResponseTest {
    @Test
    void builder() {
        assertThat(GetCapabilitiesResponse.builder().build(), instanceOf(GetCapabilitiesResponse.class));
    }
}