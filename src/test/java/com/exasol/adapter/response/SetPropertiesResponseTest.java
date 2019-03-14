package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class SetPropertiesResponseTest {
    @Test
    void build() {
        assertThat(SetPropertiesResponse.builder().build(), instanceOf(SetPropertiesResponse.class));
    }
}