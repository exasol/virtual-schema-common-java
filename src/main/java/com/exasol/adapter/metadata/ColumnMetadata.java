package com.exasol.adapter.metadata;

import java.util.Objects;

/**
 * Represents the metadata of an EXASOL table column.
 */
public class ColumnMetadata {
    private final String name;
    private final String adapterNotes;
    private final DataType type;
    private final boolean isNullable;
    private final boolean isIdentity;
    private final String defaultValue;
    private final String comment;

    public ColumnMetadata(final String name, final String adapterNotes, final DataType type, final boolean nullable,
          final boolean isIdentity, final String defaultValue, final String comment) {
        this.name = name;
        this.adapterNotes = adapterNotes;
        this.type = type;
        this.isNullable = nullable;
        this.isIdentity = isIdentity;
        this.defaultValue = defaultValue;
        this.comment = comment;
    }

    public String getName() {
        return this.name;
    }

    public String getAdapterNotes() {
        return this.adapterNotes;
    }

    public DataType getType() {
        return this.type;
    }

    public boolean isNullable() {
        return this.isNullable;
    }

    public boolean isIdentity() {
        return this.isIdentity;
    }

    public boolean hasDefault() {
        return !this.defaultValue.isEmpty();
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public boolean hasComment() {
        return !this.comment.isEmpty();
    }

    public String getComment() {
        return this.comment;
    }


    @Override
    public String toString() {
        return "ColumnMetadata{" + "name='" + this.name + '\'' + ", adapterNotes='" + this.adapterNotes + '\'' +
              ", type=" + this.type + ", isNullable=" + this.isNullable + ", isIdentity=" + this.isIdentity +
              ", defaultValue='" + this.defaultValue + '\'' + ", comment='" + this.comment + '\'' + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ColumnMetadata that = (ColumnMetadata) o;
        return this.isNullable == that.isNullable && this.isIdentity == that.isIdentity && Objects.equals(this.name, that.name) &&
              Objects.equals(this.adapterNotes, that.adapterNotes) && Objects.equals(this.type, that.type) &&
              Objects.equals(this.defaultValue, that.defaultValue) && Objects.equals(this.comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.adapterNotes, this.type, this.isNullable, this.isIdentity,
              this.defaultValue, this.comment);
    }
}
