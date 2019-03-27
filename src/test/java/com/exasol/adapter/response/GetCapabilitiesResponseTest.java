package com.exasol.adapter.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.capabilities.Capabilities;

class GetCapabilitiesResponseTest {
    @Test
    void builder() {
        assertThat(GetCapabilitiesResponse.builder().capabilities(Capabilities.builder().build()).build(),
                instanceOf(GetCapabilitiesResponse.class));
    }

    @Test
    void testGetCapabilitiesResponseHasEmptyListByDefault() {
        final GetCapabilitiesResponse response = GetCapabilitiesResponse.builder().build();
        assertThat(response.getCapabilities().isEmpty(), equalTo(true));
    }
}