package com.exasol.adapter;

/**
 * Customer exception for Virtual Schema adapters
 */
public class AdapterException extends Exception {
    private static final long serialVersionUID = -5821711270758573195L;

    /**
     * Create a new {@link AdapterException}
     *
     * @param message error message
     */
    public AdapterException(final String message) {
        super(message);
    }

    /**
     * Create a new {@link AdapterException}
     *
     * @param message error message
     * @param cause   cause for the exception
     */
    public AdapterException(final String message, final Exception cause) {
        super(message, cause);
    }
}