package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.converter.ResponseException;
import com.exasol.errorreporting.ExaError;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * create a new virtual schema.
 */
public final class CreateVirtualSchemaResponse extends AbstractResponse {
    private CreateVirtualSchemaResponse(final Builder builder) {
        super(builder.schemaMetadata);
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
     * Builder for {@link CreateVirtualSchemaResponse}
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
         * Create new {@link CreateVirtualSchemaResponse} instance
         *
         * @return new {@link CreateVirtualSchemaResponse} instance
         */
        public CreateVirtualSchemaResponse build() {
            validate(this.schemaMetadata);
            return new CreateVirtualSchemaResponse(this);
        }

        private void validate(final SchemaMetadata schemaMetadata) {
            if (schemaMetadata == null) {
                throw new ResponseException(ExaError.messageBuilder("E-VSCOMJAVA-21")
                        .message("SchemaMetadata must not be null.") //
                        .mitigation("Please, add SchemaMetadata using 'schemaMetadata(yourSchemaMetadata)' method "
                                + "of this builder before you build.")
                        .toString());
            }
        }
    }
}
