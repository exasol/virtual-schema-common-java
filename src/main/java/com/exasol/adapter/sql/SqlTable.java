package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.TableMetadata;

public class SqlTable extends SqlNode {
    private final String name;
    private final String alias;   // what is the exact semantic of this? Currently simply to generate a query with the expected alias.
    private final TableMetadata metadata;

    public SqlTable(final String name, final TableMetadata metadata) {
        this.name = name;
        this.alias = name;
        this.metadata = metadata;
    }

    public SqlTable(final String name, final String alias, final TableMetadata metadata) {
        this.name = name;
        this.alias = alias;
        this.metadata = metadata;
    }
    
    public boolean hasAlias() {
        return !name.equals(alias);
    }
    
    public String getName() {
        return name;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public TableMetadata getMetadata() {
        return metadata;
    }
    
    @Override
    public String toSimpleSql() {
        return "\"" + name.replace("\"", "\"\"") + "\"";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.TABLE;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
