package com.exasol.adapter.response.converter;

/**
 * Response Exception.
 */
public class ResponseException extends RuntimeException {
    private static final long serialVersionUID = -1895824914269433097L;

    /**
     * Instantiates a new Response exception.
     *
     * @param message the message
     */
    public ResponseException(final String message) {
        super(message);
    }
}