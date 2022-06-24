package com.exasol.adapter.metadata;

import java.util.List;

/**
 * Represents the metadata of an EXASOL Virtual Schema, including tables and columns. These metadata are are returned by
 * the Adapter when creating a virtual schema or when the adapter updates the metadata (during refresh or set property).
 */
public class SchemaMetadata {
    private final String adapterNotes;
    private final List<TableMetadata> tables;

    /**
     * Instantiates a new Schema metadata.
     *
     * @param adapterNotes the adapter notes
     * @param tables       the tables
     */
    public SchemaMetadata(final String adapterNotes, final List<TableMetadata> tables) {
        this.adapterNotes = adapterNotes;
        this.tables = tables;
    }

    /**
     * Gets adapter notes.
     *
     * @return the adapter notes
     */
    public String getAdapterNotes() {
        return this.adapterNotes;
    }

    /**
     * Gets tables.
     *
     * @return the tables
     */
    public List<TableMetadata> getTables() {
        return this.tables;
    }
}
