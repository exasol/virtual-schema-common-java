package com.exasol.adapter.metadata;

import java.util.Objects;

/**
 * Represents the metadata of an EXASOL table column.
 */
public final class ColumnMetadata {
    private final String name;
    private final String adapterNotes;
    private final DataType type;
    private final boolean nullable;
    private final boolean identity;
    private final String defaultValue;
    private final boolean defaultValueExplicitlySet;
    private final String comment;

    /**
     * Create a new column metadata instance
     *
     * @param name         column name
     * @param adapterNotes notes the adapter associates with the column
     * @param type         column data type
     * @param nullable     <code>true</code> if the column is allowed to be <code>NULL</code>
     * @param isIdentity   <code>true</code> if the column supports auto-incrementing values
     * @param defaultValue value inserted unless explicitly specified otherwise
     * @param comment      column description
     * @deprecated since version 1.9.0 use the builder that comes with this class instead.
     */
    @Deprecated
    public ColumnMetadata(final String name, final String adapterNotes, final DataType type, final boolean nullable,
            final boolean identity, final String defaultValue, final String comment) {
        this.name = name;
        this.adapterNotes = adapterNotes;
        this.type = type;
        this.nullable = nullable;
        this.identity = identity;
        this.defaultValue = defaultValue;
        this.comment = comment;
        // One reason why we should not use this method anymore is that we can't tell whether or not the default
        // value was explicitly set or not. The reason is that 'null' is a completely valid default values for any
        // type of column and an empty string at least for CHAR and VARCHAR columns.
        this.defaultValueExplicitlySet = true;
    }

    private ColumnMetadata(final Builder builder) {
        this.name = builder.name;
        this.adapterNotes = builder.adapterNotes;
        this.type = builder.type;
        this.nullable = builder.nullable;
        this.identity = builder.identity;
        this.defaultValue = builder.defaultValue;
        this.defaultValueExplicitlySet = builder.defaultValueExplicitlySet;
        this.comment = builder.comment;
    }

    /**
     * Get the column name
     *
     * @return column name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the adapter notes
     *
     * @return adapter notes
     */
    public String getAdapterNotes() {
        return this.adapterNotes;
    }

    /**
     * Get the column's data type
     *
     * @return data type
     */
    public DataType getType() {
        return this.type;
    }

    /**
     * Check if the column is nullable
     *
     * @return <code>true</code> if the column can contain <code>NULL</code>
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Check if the column supports auto-increment
     *
     * @return <code>true</code> if the column contains an auto-incrementing ID
     */
    public boolean isIdentity() {
        return this.identity;
    }

    /**
     * Check if a default value is defined for the column
     *
     * @return <code>true</code> if a default value is defined for the column
     */
    public boolean hasDefault() {
        return this.defaultValueExplicitlySet;
    }

    /**
     * Get the default value
     *
     * @return default value
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Check if a comment is associated with the column
     *
     * @return <code>true</code> if the column has a comment
     */
    public boolean hasComment() {
        return !this.comment.isEmpty();
    }

    /**
     * Get the comment associated with the column
     *
     * @return comment associated with the column
     */
    public String getComment() {
        return this.comment;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("ColumnMetadata{name=\"");
        builder.append(this.name);
        builder.append("\", adapterNotes=\"");
        builder.append(this.adapterNotes);
        builder.append("\"");
        builder.append(", type=");
        builder.append(this.type);
        builder.append(", isNullable=");
        builder.append(this.nullable);
        builder.append(", isIdentity=");
        builder.append(this.identity);
        if (this.hasDefault()) {
            builder.append(", defaultValue=\"");
            builder.append(this.defaultValue);
        }
        if (this.hasComment()) {
            builder.append("\", comment=\"");
            builder.append(this.comment);
            builder.append("\"");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        final ColumnMetadata that = (ColumnMetadata) o;
        return (this.nullable == that.nullable) && (this.identity == that.identity)
                && Objects.equals(this.name, that.name) && Objects.equals(this.adapterNotes, that.adapterNotes)
                && Objects.equals(this.type, that.type) && Objects.equals(this.defaultValue, that.defaultValue)
                && Objects.equals(this.defaultValueExplicitlySet, that.defaultValueExplicitlySet)
                && Objects.equals(this.comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.adapterNotes, this.type, this.nullable, this.identity, this.defaultValue,
                this.defaultValueExplicitlySet, this.comment);
    }

    /**
     * Create a new builder for {@link ColumnMetadata}
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link ColumnMetadata}
     */
    public static class Builder {
        private String name = null;
        private DataType type = null;
        private String adapterNotes = "";
        private boolean nullable = true;
        private boolean identity = false;
        private String defaultValue = null;
        private boolean defaultValueExplicitlySet = false;
        private String comment = "";

        /**
         * Set the column name
         *
         * @param name column name
         * @return builder instance for fluent programming
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the adapter notes
         *
         * @param adapterNotes adapter notes
         * @return builder instance for fluent programming
         */
        public Builder adapterNotes(final String adapterNotes) {
            this.adapterNotes = adapterNotes;
            return this;
        }

        /**
         * Set the column's data type
         *
         * @param type data type
         * @return builder instance for fluent programming
         */
        public Builder type(final DataType type) {
            this.type = type;
            return this;
        }

        /**
         * Define whether the column can be <code>NULL</code>
         *
         * @param nullable <code>true</code> if the column is allowed to be <code>NULL</code>
         * @return builder instance for fluent programming
         */
        public Builder nullable(final boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        /**
         * Define whether the column support auto-incrementing
         *
         * @param identity <code>true</code> if the column is an auto-incrementing ID
         * @return builder instance for fluent programming
         */
        public Builder identity(final boolean identity) {
            this.identity = identity;
            return this;
        }

        /**
         * Set the default value for the column
         *
         * @param defaultValue default value inserted when the value is not explicitly specified in the
         *                     <code>INSERT</code> command
         * @return builder instance for fluent programming
         */
        public Builder defaultValue(final String defaultValue) {
            this.defaultValue = defaultValue;
            this.defaultValueExplicitlySet = true;
            return this;
        }

        /**
         * Set the column comment
         *
         * @param comment description of the column
         * @return builder instance for fluent programming
         */
        public Builder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Build a new instance of {@link ColumnMetadata}
         *
         * @return new instance
         */
        public ColumnMetadata build() {
            validateName();
            validateType();
            return new ColumnMetadata(this);
        }

        private void validateName() {
            if ((this.name == null) || this.name.isEmpty()) {
                throw new IllegalStateException(
                        "Failed to build column metadata because mandatory column name was missing");
            }
        }

        private void validateType() {
            if (this.type == null) {
                throw new IllegalStateException(
                        "Failed to build column \"" + this.name + "\" metadata because mandatory data type is missng");
            }
        }
    }
}