package com.exasol.adapter.response;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class RefreshResponseTest {
    @Test
    void builder() {
        assertThat(RefreshResponse.builder().build(), instanceOf(RefreshResponse.class));
    }
}