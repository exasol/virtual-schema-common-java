package com.exasol.adapter.request.parser;

/**
 * This class represent exceptions thrown when a Virtual Schema request cannot be parsed.
 *
 * <p>
 * This exception is not recoverable in the Virtual Schema Adapter.
 */
public class RequestParserException extends RuntimeException {
    private static final long serialVersionUID = -4948650352627377411L;

    /**
     * Create a new {@link RequestParserException} instance
     *
     * @param message error message
     */
    public RequestParserException(final String message) {
        super(message);
    }
}