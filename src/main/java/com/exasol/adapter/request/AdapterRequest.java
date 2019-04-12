package com.exasol.adapter.request;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * Common interface for Virtual Schema Adapter requests
 */
public interface AdapterRequest {
    /**
     * Get the schema metadata that serves as context around the Virtual Schema
     *
     * @return schema metadata
     */
    public SchemaMetadataInfo getSchemaMetadataInfo();

    /**
     * Get the request type
     *
     * @return request type
     */
    public AdapterRequestType getType();

    /**
     * Get the name of the adapter that should handle the request
     *
     * @return adapter name
     */
    public String getAdapterName();

    /**
     * Get the name of the Virtual Schema that this request is addressing
     *
     * @return Virtual Schema name
     */
    public String getVirtualSchemaName();
}