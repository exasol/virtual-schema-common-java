package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * set properties.
 */
public final class SetPropertiesResponse extends AbstractResponse {
    private SetPropertiesResponse(final Builder builder) {
        super(builder.schemaMetadata);
    }

    /**
     * Get a {@link SetPropertiesResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link SetPropertiesResponse}
     */
    public static class Builder {
        private SchemaMetadata schemaMetadata;

        /**
         * Add the Virtual Schema's metadata
         *
         * @param schemaMetadata Virtual Schema's metadata
         * @return builder instance for fluent programming
         */
        public Builder schemaMetadata(final SchemaMetadata schemaMetadata) {
            this.schemaMetadata = schemaMetadata;
            return this;
        }

        /**
         * Create new {@link SetPropertiesResponse} instance
         *
         * @return new {@link SetPropertiesResponse} instance
         */
        public SetPropertiesResponse build() {
            return new SetPropertiesResponse(this);
        }
    }
}
