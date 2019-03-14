package com.exasol.adapter.response;

import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.response.converter.ResponseException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetCapabilitiesResponseTest {
    @Test
    void builder() {
        assertThat(GetCapabilitiesResponse.builder().capabilities(Capabilities.builder().build()).build(),
              instanceOf(GetCapabilitiesResponse.class));
    }

    @Test
    void testGetCapabilitiesResponseThrowsException() {
        assertThrows(ResponseException.class, GetCapabilitiesResponse.builder()::build);
    }
}