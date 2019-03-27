package com.exasol.adapter.request;

import java.util.List;

import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to present the current metadata
 */
public class RefreshRequest extends AbstractAdapterRequest {
    private final boolean refreshSelectedTablesOnly;
    private List<String> tables;

    /**
     * Create a new request of type {@link RefreshRequest} for the whole Virtual Schema
     *
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     */
    public RefreshRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.REFRESH);
        this.refreshSelectedTablesOnly = false;
    }

    /**
     * Create a new request of type {@link RefreshRequest} for selected tables
     *
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     * @param tables             tables for which the metadata should be refreshed
     */
    public RefreshRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo,
            final List<String> tables) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.REFRESH);
        if ((tables == null) || tables.isEmpty()) {
            throw new IllegalArgumentException(
                    "The RefreshRequest constructor expects a list of requested tables, but the list is currently empty.");
        }
        this.refreshSelectedTablesOnly = true;
        this.tables = tables;
    }

    /**
     * Get the tables for which the metadata should be presented
     *
     * @return tables
     */
    public List<String> getTables() {
        return this.tables;
    }

    /**
     * @return <code>true</code> if the refresh request is restricted to tables
     */
    public boolean refreshesOnlySelectedTables() {
        return this.refreshSelectedTablesOnly;
    }
}