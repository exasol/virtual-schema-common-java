package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.CreateVirtualSchemaRequest;
import com.exasol.adapter.request.DropVirtualSchemaRequest;

/**
 * Interface for Virtual Schema Adapters.
 *
 * <p>
 * This interface provides a number of handler functions for different types of
 * requests a virtual schema adapter can receive from the Exasol core database.
 */
public interface VirtualSchemaAdapter {
    /**
     * Drop an existing Virtual Schema
     *
     * @param metadata context metadata
     * @param request  request containing the details of the Virtual Schema to be
     *                 dropped
     */
    public void dropVirtualSchema(final ExaMetadata metadata, final DropVirtualSchemaRequest request);

    /**
     * Create a Virtual Schema
     *
     * @param metadata context metadata
     * @param request  request containing the details of the Virtual Schema to be
     *                 created
     */
    public void createVirtualSchema(final ExaMetadata metadata, final CreateVirtualSchemaRequest request);
}