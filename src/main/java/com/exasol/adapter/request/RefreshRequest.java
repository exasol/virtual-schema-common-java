package com.exasol.adapter.request;

import java.util.List;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

public class RefreshRequest extends AdapterRequest {

    private boolean isRefreshForTables;
    private List<String> tables;

    public RefreshRequest(SchemaMetadataInfo schemaMetadataInfo) {
        super(schemaMetadataInfo, AdapterRequestType.REFRESH);
        isRefreshForTables = false;
    }

    public RefreshRequest(SchemaMetadataInfo schemaMetadataInfo, List<String> tables) throws AdapterException {
        super(schemaMetadataInfo, AdapterRequestType.REFRESH);
        if (tables == null || tables.isEmpty()) {
            throw new AdapterException("The list of tables is empty.");
        }
        isRefreshForTables = true;
        this.tables = tables;
    }

    public List<String> getTables() {
        return tables;
    }

    public boolean isRefreshForTables() {
        return isRefreshForTables;
    }

}
