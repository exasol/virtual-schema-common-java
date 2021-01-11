package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;
import com.exasol.adapter.response.converter.ResponseException;
import com.exasol.errorreporting.ExaError;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * refresh the virtual schema.
 */
public final class RefreshResponse extends AbstractResponse {
    private RefreshResponse(final Builder builder) {
        super(builder.schemaMetadata);
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
     * Builder for {@link RefreshResponse}
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
         * Create new {@link RefreshResponse} instance
         *
         * @return new {@link RefreshResponse} instance
         */
        public RefreshResponse build() {
            validate(this.schemaMetadata);
            return new RefreshResponse(this);
        }

        private void validate(final SchemaMetadata schemaMetadata) {
            if (schemaMetadata == null) {
                throw new ResponseException(ExaError.messageBuilder("E-VS-COM-JAVA-23") //
                        .message("SchemaMetadata should be not null.")
                        .mitigation("Please, add SchemaMetadata using 'schemaMetadata(yourSchemaMetadata)' "
                                + "method of this builder before you build.")
                        .toString());
            }
        }
    }
}
