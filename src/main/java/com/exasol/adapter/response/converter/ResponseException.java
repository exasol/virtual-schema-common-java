package com.exasol.adapter.response.converter;

public class ResponseException extends RuntimeException {
    public ResponseException(final String message) {
        super(message);
    }
}
