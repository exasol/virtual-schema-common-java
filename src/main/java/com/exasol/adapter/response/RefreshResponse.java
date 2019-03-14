package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;

/**
 * This class is an abstract representation of a response
 * created by a Virtual Schema Adapter as result of a request to refresh the virtual schema.
 */
public final class RefreshResponse {
    private final SchemaMetadata schemaMetadata;

    private RefreshResponse(final Builder builder) {
        this.schemaMetadata = builder.schemaMetadata;
    }

    /**
     * Get a {@link RefreshResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the Virtual Schema's refresh metadata
     *
     * @return schema metadata
     */
    public SchemaMetadata getSchemaMetadata() {
        return this.schemaMetadata;
    }

    /**
     * Builder for {@link RefreshResponse}
     */
    public static class Builder {
        SchemaMetadata schemaMetadata;

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
         * Create new {@link RefreshResponse} instance
         *
         * @return new {@link RefreshResponse} instance
         */
        public RefreshResponse build() {
            return new RefreshResponse(this);
        }
    }
}
