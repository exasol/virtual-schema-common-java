package com.exasol.adapter.response;

import com.exasol.adapter.metadata.SchemaMetadata;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter.
 */
class AbstractResponse {
    private final SchemaMetadata schemaMetadata;

    AbstractResponse(final SchemaMetadata schemaMetadata) {
        this.schemaMetadata = schemaMetadata;
    }

    /**
     * Get the Virtual Schema's metadata
     *
     * @return schema metadata
     */
    public SchemaMetadata getSchemaMetadata() {
        return this.schemaMetadata;
    }
}
