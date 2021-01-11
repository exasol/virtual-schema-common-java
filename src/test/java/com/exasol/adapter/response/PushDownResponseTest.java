package com.exasol.adapter.response;

import com.exasol.adapter.response.converter.ResponseException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PushDownResponseTest {
    @Test
    void builder() {
        assertThat(PushDownResponse.builder().pushDownSql("PUSH DOWN").build(), instanceOf(PushDownResponse.class));
    }

    @Test
    void testPushDownResponseThrowsException() {
        final ResponseException exception = assertThrows(ResponseException.class, PushDownResponse.builder()::build);
        assertThat(exception.getMessage(), containsString("E-VS-COM-JAVA-22"));
    }
}