package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;

public class SetPropertiesResponse {
    private final SchemaMetadata schemaMetadata;

    private SetPropertiesResponse(final Builder builder) {
        this.schemaMetadata = builder.schemaMetadata;
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
     * Get the Virtual Schema's set properties metadata
     *
     * @return schema metadata
     */
    public SchemaMetadata getSchemaMetadata() {
        return this.schemaMetadata;
    }

    /**
     * Builder for {@link SetPropertiesResponse}
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
         * Create new {@link SetPropertiesResponse} instance
         *
         * @return new {@link SetPropertiesResponse} instance
         */
        public SetPropertiesResponse build() {
            return new SetPropertiesResponse(this);
        }
    }
}
