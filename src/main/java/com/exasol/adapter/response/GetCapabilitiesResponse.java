package com.exasol.adapter.response;

import com.exasol.adapter.capabilities.Capabilities;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * get capabilities.
 */
public final class GetCapabilitiesResponse {
    private final Capabilities capabilities;

    private GetCapabilitiesResponse(final Builder builder) {
        this.capabilities = builder.capabilities;
    }

    /**
     * Get a {@link GetCapabilitiesResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the list of capabilities supported by the Adapter
     *
     * @return {@link Capabilities} instance
     */
    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    /**
     * Builder for {@link GetCapabilitiesResponse}
     */
    public static class Builder {
        private Capabilities capabilities;

        /**
         * Add capabilities
         *
         * @param capabilities the list of capabilities supported by the Adapter
         * @return builder instance for fluent programming
         */
        public Builder capabilities(final Capabilities capabilities) {
            this.capabilities = capabilities;
            return this;
        }

        /**
         * Create new {@link GetCapabilitiesResponse} instance
         *
         * @return new {@link GetCapabilitiesResponse} instance
         */
        public GetCapabilitiesResponse build() {
            if (this.capabilities == null) {
                this.capabilities = Capabilities.builder().build();
            }
            return new GetCapabilitiesResponse(this);
        }
    }
}
