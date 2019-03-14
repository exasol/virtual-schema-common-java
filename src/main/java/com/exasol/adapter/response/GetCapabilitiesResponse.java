package com.exasol.adapter.response;

import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.response.converter.ResponseException;

/**
 * This class is an abstract representation of a response
 * created by a Virtual Schema Adapter as result of a request to get capabilities.
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
            validate(this.capabilities);
            return new GetCapabilitiesResponse(this);
        }

        private void validate(final Capabilities capabilities) {
            if (capabilities == null) {
                throw new ResponseException("Capabilities should be not null. Please, add Capabilities using "  //
                      + "'capabilities(yourCapabilities)' method of this builder before you build.");
            }
        }
    }
}
