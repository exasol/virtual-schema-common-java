package com.exasol.adapter.request;

import java.util.List;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.*;
import com.exasol.adapter.sql.SqlStatement;

/**
 * This class represents a request that tells a Virtual Schema Adapter to push a SQL statement down to the external data
 * source.
 */
public class PushDownRequest extends AbstractAdapterRequest {
    private final SqlStatement select;
    private final List<TableMetadata> involvedTablesMetadata;
    private final List<DataType> selectListDataTypes;

    /**
     * Create a new request of type {@link PushDownRequest}.
     *
     * @param schemaMetadataInfo     schema metadata
     * @param select                 SQL statement to be pushed down to the external data source
     * @param involvedTablesMetadata tables involved in the push-down request
     * @param selectListDataType     expected data types for the result set
     */
    public PushDownRequest(final SchemaMetadataInfo schemaMetadataInfo, final SqlStatement select,
            final List<TableMetadata> involvedTablesMetadata, final List<DataType> selectListDataType) {
        super(schemaMetadataInfo, AdapterRequestType.PUSHDOWN);
        this.select = select;
        this.involvedTablesMetadata = involvedTablesMetadata;
        this.selectListDataTypes = selectListDataType;
    }

    /**
     * Get the <code>SELECT</code> statement that should be pushed down to the external data source.
     *
     * @return <code>SELECT</code> statement
     */
    public SqlStatement getSelect() {
        return this.select;
    }

    /**
     * Get the metadata for the tables involved in the pushdown operation
     *
     * @return metadata of involved tables
     */
    public List<TableMetadata> getInvolvedTablesMetadata() {
        return this.involvedTablesMetadata;
    }

    /**
     * Get the expected data types for the result set.
     *
     * @return expected data types for the result set
     */
    public List<DataType> getSelectListDataTypes() {
        return this.selectListDataTypes;
    }

    @Override
    public String executeWith(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executePushDownRequest(this, metadata);
    }
}