package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.TableMetadata;

/**
 * This class represents a SQL table.
 */
public class SqlTable extends SqlNode {
    private final String name;
    private final String alias;
    private final TableMetadata metadata;

    /**
     * Instantiates a new Sql table.
     *
     * @param name     the name
     * @param metadata the metadata
     */
    public SqlTable(final String name, final TableMetadata metadata) {
        this.name = name;
        this.alias = name;
        this.metadata = metadata;
    }

    /**
     * Instantiates a new Sql table.
     *
     * @param name     the name
     * @param alias    the alias
     * @param metadata the metadata
     */
    public SqlTable(final String name, final String alias, final TableMetadata metadata) {
        this.name = name;
        this.alias = alias;
        this.metadata = metadata;
    }

    /**
     * Has alias boolean.
     *
     * @return the boolean
     */
    public boolean hasAlias() {
        return !this.name.equals(this.alias);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
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