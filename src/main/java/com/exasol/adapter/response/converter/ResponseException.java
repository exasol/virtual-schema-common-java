package com.exasol.adapter.response.converter;

public class ResponseException extends RuntimeException {
    private static final long serialVersionUID = -1895824914269433097L;

    public ResponseException(final String message) {
        super(message);
    }
}