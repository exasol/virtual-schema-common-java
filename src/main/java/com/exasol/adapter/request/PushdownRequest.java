package com.exasol.adapter.request;

import java.util.List;

import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.sql.SqlStatement;

public class PushdownRequest extends AdapterRequest {
    private final SqlStatement select;
    private final List<TableMetadata> involvedTablesMetadata;
    
    public PushdownRequest(final SchemaMetadataInfo schemaMetadataInfo, final SqlStatement select, final List<TableMetadata> involvedTablesMetadata) {
        super(schemaMetadataInfo, AdapterRequestType.PUSHDOWN);
        this.select = select;
        this.involvedTablesMetadata = involvedTablesMetadata;
    }
    
    public SqlStatement getSelect() {
        return select;
    }

    public List<TableMetadata> getInvolvedTablesMetadata() {
        return involvedTablesMetadata;
    }
}
