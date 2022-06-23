package com.exasol.adapter.request;

import java.util.List;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.errorreporting.ExaError;

/**
 * This class represents a request that tells a Virtual Schema Adapter to present the current metadata
 */
public class RefreshRequest extends AbstractAdapterRequest {
    private final boolean refreshSelectedTablesOnly;
    private List<String> tables;

    /**
     * Create a new request of type {@link RefreshRequest} for the whole Virtual Schema
     *
     * @param schemaMetadataInfo schema metadata
     */
    public RefreshRequest(final SchemaMetadataInfo schemaMetadataInfo) {
        super(schemaMetadataInfo, AdapterRequestType.REFRESH);
        this.refreshSelectedTablesOnly = false;
    }

    /**
     * Create a new request of type {@link RefreshRequest} for selected tables
     *
     * @param schemaMetadataInfo schema metadata
     * @param tables             tables for which the metadata should be refreshed
     */
    public RefreshRequest(final SchemaMetadataInfo schemaMetadataInfo, final List<String> tables) {
        super(schemaMetadataInfo, AdapterRequestType.REFRESH);
        if ((tables == null) || tables.isEmpty()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-32")
                    .message("The RefreshRequest constructor expects a list of requested tables, "
                            + "but the list is currently empty.")
                    .toString());
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

    @Override
    public String executeWith(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeRefreshRequest(this, metadata);
    }
}