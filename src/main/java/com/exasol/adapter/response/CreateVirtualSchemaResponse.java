package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.converter.ResponseJsonConverter;

/**
 * Used in overloaded convert() method of {@link ResponseJsonConverter}
 */
public final class CreateVirtualSchemaResponse {
    private final SchemaMetadata schemaMetadata;

    private CreateVirtualSchemaResponse(final Builder builder) {
        this.schemaMetadata = builder.schemaMetadata;
    }

    /**
     * Get a {@link CreateVirtualSchemaResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the Virtual Schema's metadata
     *
     * @return schema metadata
     */
    public SchemaMetadata getSchemaMetadata() {
        return this.schemaMetadata;
    }

    /**
     * Builder for {@link CreateVirtualSchemaResponse}
     */
    public static class Builder {
        SchemaMetadata schemaMetadata;

        /**
         * Add the Virtual Schema's metadata
         *
         * @return builder instance for fluent programming
         */
        public Builder addSchemaMetadata(final SchemaMetadata schemaMetadata) {
            this.schemaMetadata = schemaMetadata;
            return this;
        }

        /**
         * Create new {@link CreateVirtualSchemaResponse} instance
         *
         * @return new {@link CreateVirtualSchemaResponse} instance
         */
        public CreateVirtualSchemaResponse build() {
            return new CreateVirtualSchemaResponse(this);
        }
    }
}
