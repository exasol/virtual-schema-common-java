package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.response.*;

/**
 * Interface for Virtual Schema Adapters.
 *
 * <p>
 * This interface provides a number of handler functions for different types of requests a virtual schema adapter can
 * receive from the Exasol core database.
 */
public interface VirtualSchemaAdapter {
    /**
     * Create a Virtual Schema
     *
     * @param metadata context metadata
     * @param request  request containing the details of the Virtual Schema to be created
     * @return response containing metadata of the created schema
     */
    public CreateVirtualSchemaResponse createVirtualSchema(final ExaMetadata metadata,
            final CreateVirtualSchemaRequest request);

    /**
     * Drop an existing Virtual Schema
     *
     * @param metadata context metadata
     * @param request  request containing the details of the Virtual Schema to be dropped
     * @return drop response
     */
    public DropVirtualSchemaResponse dropVirtualSchema(final ExaMetadata metadata,
            final DropVirtualSchemaRequest request);

    /**
     * Refresh the Virtual Schema metadata for a given set of tables
     *
     * @param metadata context metadata
     * @param request  request containing the list of tables for which the metadata should be refreshed
     * @return response containing up-to-date metadata for the given tables
     */
    public RefreshResponse refresh(final ExaMetadata metadata, final RefreshRequest request);

    /**
     * Set Virtual Schema properties
     *
     * @param metadata context metadata
     * @param request  request containing the properties to be set
     * @return property setting response
     */
    public SetPropertiesResponse setProperties(final ExaMetadata metadata, final SetPropertiesRequest request);

    /**
     * Get the capabilities the adapter registered for a Virtual Schema supports
     *
     * @param metadata context metadata
     * @param request  capabilities request
     * @return response containing the capabilities of the adapter
     */
    public GetCapabilitiesResponse getCapabilities(final ExaMetadata metadata, final GetCapabilitiesRequest request);

    /**
     * Create a push down request
     *
     * @param metadata context metadata
     * @param request  request containing the SQL statement to be pushed down to the external source
     * @return response containing the pushdown SQL statement
     */
    public PushDownResponse pushdown(final ExaMetadata metadata, final PushDownRequest request);
}