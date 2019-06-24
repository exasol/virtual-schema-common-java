package com.exasol.adapter.response;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * drop a virtual schema.
 */
public final class DropVirtualSchemaResponse {
    /**
     * Get a {@link DropVirtualSchemaResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    private DropVirtualSchemaResponse() {
        // intentionally left blank
    }

    /**
     * Builder for {@link DropVirtualSchemaResponse}
     */
    public static final class Builder {
        /**
         * Create new {@link DropVirtualSchemaResponse} instance
         *
         * @return new {@link DropVirtualSchemaResponse} instance
         */
        @SuppressWarnings("squid:S2440")
        public DropVirtualSchemaResponse build() {
            return new DropVirtualSchemaResponse();
        }
    }
}
