package com.exasol.adapter.json;

import javax.json.*;

import com.exasol.adapter.metadata.*;
import com.exasol.utils.JsonHelper;

public final class SchemaMetadataSerializer {
    private static final String ADAPTER_NOTES = "adapterNotes";

    private SchemaMetadataSerializer() {
        // Intentionally left blank
    }

    public static JsonObjectBuilder serialize(final SchemaMetadata schema) {
        final JsonBuilderFactory factory = JsonHelper.getBuilderFactory();
        final JsonObjectBuilder root = factory.createObjectBuilder();
        final JsonArrayBuilder tablesBuilder = factory.createArrayBuilder();
        for (final TableMetadata table : schema.getTables()) {
            tablesBuilder.add(serializeTableMetadata(table, factory.createObjectBuilder()));
        }
        root.add("tables", tablesBuilder);
        root.add(ADAPTER_NOTES, schema.getAdapterNotes());
        return root;
    }

    private static JsonObjectBuilder serializeTableMetadata(final TableMetadata table,
            final JsonObjectBuilder tableBuilder) {
        tableBuilder.add("type", "table");
        tableBuilder.add("name", table.getName());
        final JsonArrayBuilder columnsBuilder = Json.createArrayBuilder();
        for (final ColumnMetadata column : table.getColumns()) {
            columnsBuilder.add(serializeColumnMetadata(column, Json.createObjectBuilder()));
        }
        final String adapterNotes = table.getAdapterNotes();
        if ((adapterNotes != null) && !adapterNotes.isEmpty()) {
            tableBuilder.add(ADAPTER_NOTES, adapterNotes);
        }
        if (table.hasComment()) {
            tableBuilder.add("comment", table.getComment());
        }
        tableBuilder.add("columns", columnsBuilder);
        return tableBuilder;
    }

    private static JsonObjectBuilder serializeColumnMetadata(final ColumnMetadata column,
            final JsonObjectBuilder columnBuilder) {
        columnBuilder.add("name", column.getName());
        columnBuilder.add(ADAPTER_NOTES, column.getAdapterNotes());
        columnBuilder.add("dataType", SqlDataTypeJsonSerializer.serialize(column.getType()));
        if (!column.isNullable()) {
            columnBuilder.add("isNullable", false);
        }
        if (column.isIdentity()) {
            columnBuilder.add("isIdentity", true);
        }
        if (column.hasDefault()) {
            columnBuilder.add("default", column.getDefaultValue());
        }
        if (column.hasComment()) {
            columnBuilder.add("comment", column.getComment());
        }
        return columnBuilder;
    }
}
