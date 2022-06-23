package com.exasol.adapter.metadata;

import java.util.*;

/**
 * Represents the metadata of an EXASOL table.
 */
public class TableMetadata {
    private final String name;
    private final String adapterNotes;
    private final List<ColumnMetadata> columns;
    private final String comment;

    /**
     * Instantiates a new Table metadata.
     *
     * @param name         the name
     * @param adapterNotes the adapter notes
     * @param columns      the columns
     * @param comment      the comment
     */
    public TableMetadata(final String name, final String adapterNotes, final List<ColumnMetadata> columns,
            final String comment) {
        this.name = name;
        this.adapterNotes = adapterNotes;
        this.columns = columns;
        this.comment = comment;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        final TableMetadata that = (TableMetadata) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.adapterNotes, that.adapterNotes)
                && Objects.equals(this.columns, that.columns) && Objects.equals(this.comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.adapterNotes, this.columns, this.comment);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TableMetadata.class.getSimpleName() + "{", "}\n") //
                .add("name=" + this.name) //
                .add("adapterNotes=" + this.adapterNotes) //
                .add("columns=" + this.columns) //
                .add("comment=" + this.comment) //
                .toString();
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
     * Gets adapter notes.
     *
     * @return the adapter notes
     */
    public String getAdapterNotes() {
        return this.adapterNotes;
    }

    /**
     * Gets columns.
     *
     * @return the columns
     */
    public List<ColumnMetadata> getColumns() {
        return this.columns;
    }

    /**
     * Gets comment.
     *
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Has comment boolean.
     *
     * @return the boolean
     */
    public boolean hasComment() {
        return (this.comment != null) && !this.comment.isEmpty();
    }

    /**
     * Has adapter note boolean.
     *
     * @return the boolean
     */
    public boolean hasAdapterNote() {
        return (this.adapterNotes != null) && !this.adapterNotes.isEmpty();
    }

    /**
     * Create a human-readable short description of the table
     *
     * @return short description
     */
    public String describe() {
        boolean first = true;
        final StringBuilder builder = new StringBuilder(this.name);
        builder.append(" (");
        for (final ColumnMetadata column : this.columns) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(column.getName());
            builder.append(" ");
            builder.append(column.getType().toString());
            first = false;
        }
        builder.append(")");
        return builder.toString();
    }
}