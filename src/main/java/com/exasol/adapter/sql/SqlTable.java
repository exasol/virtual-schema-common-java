package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.TableMetadata;

public class SqlTable extends SqlNode {
    private final String name;
    private final String alias;
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
        return !this.name.equals(this.alias);
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public TableMetadata getMetadata() {
        return this.metadata;
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