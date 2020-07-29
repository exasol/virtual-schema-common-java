package com.exasol.adapter.metadata.converter;

import java.util.Collections;

import javax.json.*;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.DataType.*;
import com.exasol.errorhandling.ErrorMessages;

/**
 * This class converts the schema metadata to its JSON representation.
 */
public final class SchemaMetadataJsonConverter {
    private static final SchemaMetadataJsonConverter instance = new SchemaMetadataJsonConverter();
    private static final String ADAPTER_NOTES_KEY = "adapterNotes";
    private static final String TABLES_KEY = "tables";
    private static final String TYPE_KEY = "type";
    private static final String TABLE_NAME_KEY = "name";
    private static final String DATA_TYPE_KEY = "dataType";
    private static final String NULLABLE_KEY = "isNullable";
    private static final String COMMENT_KEY = "comment";
    private static final String IDENTITY_KEY = "isIdentity";
    private static final String DEFAULT_KEY = "default";
    private final JsonBuilderFactory factory = Json.createBuilderFactory(Collections.emptyMap());

    /**
     * Get the singleton instance of the {@link SchemaMetadataJsonConverter}
     *
     * @return converter instance
     */
    public static SchemaMetadataJsonConverter getInstance() {
        return instance;
    }

    private SchemaMetadataJsonConverter() {
    }

    /**
     * Convert the given schema metadata to its JSON representation
     *
     * @param schemaMetadata schemaMetadata to be converted into JSON
     * @return JSON representation
     */
    public JsonObject convert(final SchemaMetadata schemaMetadata) {
        final JsonObjectBuilder root = this.factory.createObjectBuilder();
        root.add(TABLES_KEY, convertTables(schemaMetadata));
        root.add(ADAPTER_NOTES_KEY, schemaMetadata.getAdapterNotes());
        return root.build();
    }

    private JsonArrayBuilder convertTables(final SchemaMetadata schemaMetadata) {
        final JsonArrayBuilder tablesBuilder = this.factory.createArrayBuilder();
        for (final TableMetadata table : schemaMetadata.getTables()) {
            tablesBuilder.add(convertTableMetadata(table));
        }
        return tablesBuilder;
    }

    private JsonObjectBuilder convertTableMetadata(final TableMetadata table) {
        final JsonObjectBuilder tableBuilder = this.factory.createObjectBuilder();
        tableBuilder.add(TYPE_KEY, "table");
        tableBuilder.add(TABLE_NAME_KEY, table.getName());
        final JsonArrayBuilder columnsBuilder = Json.createArrayBuilder();
        for (final ColumnMetadata column : table.getColumns()) {
            columnsBuilder.add(convertColumnMetadata(column));
        }
        if (table.hasAdapterNote()) {
            tableBuilder.add(ADAPTER_NOTES_KEY, table.getAdapterNotes());
        }
        if (table.hasComment()) {
            tableBuilder.add(COMMENT_KEY, table.getComment());
        }
        tableBuilder.add("columns", columnsBuilder);
        return tableBuilder;
    }

    private JsonObjectBuilder convertColumnMetadata(final ColumnMetadata column) {
        final JsonObjectBuilder columnBuilder = this.factory.createObjectBuilder();
        columnBuilder.add(TABLE_NAME_KEY, column.getName());
        columnBuilder.add(ADAPTER_NOTES_KEY, column.getAdapterNotes());
        columnBuilder.add(DATA_TYPE_KEY, convertType(column.getType()));
        if (!column.isNullable()) {
            columnBuilder.add(NULLABLE_KEY, false);
        }
        if (column.isIdentity()) {
            columnBuilder.add(IDENTITY_KEY, true);
        }
        if (column.hasDefault()) {
            columnBuilder.add(DEFAULT_KEY, column.getDefaultValue());
        }
        if (column.hasComment()) {
            columnBuilder.add(COMMENT_KEY, column.getComment());
        }
        return columnBuilder;
    }

    /**
     * Convert a data type to its JSON representation
     *
     * @param dataType data type to be converted
     * @return JSON representation
     */
    public JsonObject convertType(final DataType dataType) {
        final JsonObjectBuilder typeAsJson = this.factory.createObjectBuilder();
        typeAsJson.add(TYPE_KEY, getExasolDataTypeName(dataType.getExaDataType()));
        switch (dataType.getExaDataType()) {
        case UNSUPPORTED:
            throw new IllegalArgumentException("Unsupported data type found trying to serialize schema metadata. "
                    + ErrorMessages.askForBugReport());
        case DECIMAL:
            typeAsJson.add("precision", dataType.getPrecision());
            typeAsJson.add("scale", dataType.getScale());
            break;
        case VARCHAR: // falling through intentionally
        case CHAR:
            typeAsJson.add("size", dataType.getSize());
            typeAsJson.add("characterSet", getCharacterSetName(dataType.getCharset()));
            break;
        case TIMESTAMP:
            typeAsJson.add("withLocalTimeZone", dataType.isWithLocalTimezone());
            break;
        case GEOMETRY:
            typeAsJson.add("srid", dataType.getGeometrySrid());
            break;
        case INTERVAL:
            addIntervalToRoot(dataType, typeAsJson);
            break;
        case HASHTYPE:
            typeAsJson.add("bytesize", dataType.getByteSize());
            break;
        case DOUBLE: // falling through intentionally
        case DATE:
        case BOOLEAN:
            break;
        default:
            throw new IllegalArgumentException("Unexpected data type \"" + dataType.getExaDataType()
                    + "\" encountered trying to serialize schema metadata.");
        }
        return typeAsJson.build();
    }

    private void addIntervalToRoot(final DataType dataType, final JsonObjectBuilder dataTypeAsJson) {
        dataTypeAsJson.add("fromTo", intervalTypeAsString(dataType.getIntervalType()));
        dataTypeAsJson.add("precision", dataType.getPrecision());
        if (dataType.getIntervalType() == IntervalType.DAY_TO_SECOND) {
            dataTypeAsJson.add("fraction", dataType.getIntervalFraction());
        }
    }

    private String getExasolDataTypeName(final ExaDataType dataType) {
        switch (dataType) {
        case UNSUPPORTED:
            return "unsupported";
        case DECIMAL:
            return "decimal";
        case DOUBLE:
            return "double";
        case VARCHAR:
            return "varchar";
        case CHAR:
            return "char";
        case DATE:
            return "date";
        case TIMESTAMP:
            return "timestamp";
        case BOOLEAN:
            return "boolean";
        case GEOMETRY:
            return "geometry";
        case INTERVAL:
            return "interval";
        case HASHTYPE:
            return "hashtype";
        default:
            return "unknown";
        }
    }

    private String getCharacterSetName(final ExaCharset charset) {
        switch (charset) {
        case UTF8:
            return "UTF8";
        case ASCII:
            return "ASCII";
        default:
            throw new IllegalArgumentException("Unexpected charset \"" + charset
                    + "\" encounted while trying to serialize character string type information. "
                    + ErrorMessages.askForBugReport());
        }
    }

    private String intervalTypeAsString(final IntervalType intervalType) {
        switch (intervalType) {
        case DAY_TO_SECOND:
            return "DAY TO SECONDS";
        case YEAR_TO_MONTH:
            return "YEAR TO MONTH";
        default:
            throw new IllegalArgumentException("Unexpected interval type \"" + intervalType
                    + "\" trying to serialize an interval. " + ErrorMessages.askForBugReport());
        }
    }
}