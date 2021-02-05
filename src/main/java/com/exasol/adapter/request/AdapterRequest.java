package com.exasol.adapter.request;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
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
     * Get the name of the Virtual Schema that this request is addressing
     *
     * @return Virtual Schema name
     */
    public String getVirtualSchemaName();

    /**
     * Execute an adapter call.
     * 
     * @param adapterCallExecutor instance of {@link AdapterCallExecutor}
     * @param metadata            metadata for the context in which the adapter exists
     * @return response in a JSON format
     * @throws AdapterException if some problem occurs
     */
    public String executeWith(AdapterCallExecutor adapterCallExecutor, ExaMetadata metadata) throws AdapterException;
}