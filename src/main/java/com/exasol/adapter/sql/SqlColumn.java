package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.ColumnMetadata;

public class SqlColumn extends SqlNode {
    private final int id;
    private final ColumnMetadata metadata;
    private String tableName;
    private String tableAlias;

    public SqlColumn(final int id, final ColumnMetadata metadata) {
        this(id, metadata, null, null);
    }

    public SqlColumn(final int id, final ColumnMetadata metadata, final String tableName) {
        this(id, metadata, tableName, null);
    }

    public SqlColumn(final int id, final ColumnMetadata metadata, final String tableName, final String tableAlias) {
        this.id = id;
        this.metadata = metadata;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
    }
    
    public int getId() {
        return id;
    }
    
    public ColumnMetadata getMetadata() {
        return metadata;
    }
    
    public String getName() {
        return metadata.getName();
    }

    public String getTableName() {
        return tableName;
    }

    public boolean hasTableAlias() { return this.tableAlias != null; }

    public String getTableAlias() {
        return tableAlias;
    }
    
    @Override
    public String toSimpleSql() {
        return "\"" + metadata.getName().replace("\"", "\"\"") + "\"";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.COLUMN;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
