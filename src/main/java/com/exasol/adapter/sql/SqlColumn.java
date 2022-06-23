package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.ColumnMetadata;

/**
 * A SQL column.
 */
public class SqlColumn extends SqlNode {
    private final int id;
    private final ColumnMetadata metadata;
    private final String tableName;
    private final String tableAlias;

    /**
     * Instantiates a new Sql column.
     *
     * @param id       the id
     * @param metadata the metadata
     */
    public SqlColumn(final int id, final ColumnMetadata metadata) {
        this(id, metadata, null, null);
    }

    /**
     * Instantiates a new Sql column.
     *
     * @param id        the id
     * @param metadata  the metadata
     * @param tableName the table name
     */
    public SqlColumn(final int id, final ColumnMetadata metadata, final String tableName) {
        this(id, metadata, tableName, null);
    }

    /**
     * Instantiates a new Sql column.
     *
     * @param id         the id
     * @param metadata   the metadata
     * @param tableName  the table name
     * @param tableAlias the table alias
     */
    public SqlColumn(final int id, final ColumnMetadata metadata, final String tableName, final String tableAlias) {
        this.id = id;
        this.metadata = metadata;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    public ColumnMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.metadata.getName();
    }

    /**
     * Gets table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Has table alias boolean.
     *
     * @return the boolean
     */
    public boolean hasTableAlias() {
        return this.tableAlias != null;
    }

    /**
     * Gets table alias.
     *
     * @return the table alias
     */
    public String getTableAlias() {
        return this.tableAlias;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.COLUMN;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SqlColumn{" + "id=" + this.id + ", metadata=" + this.metadata + ", tableName='" + this.tableName + '\''
                + ", tableAlias='" + this.tableAlias + '\'' + '}';
    }
}