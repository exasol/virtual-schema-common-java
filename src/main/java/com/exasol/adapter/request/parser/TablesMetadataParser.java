package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import java.util.*;

import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.converter.SchemaMetadataJsonConverter;
import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

/**
 * This class provides a parser for table metadata
 */
public class TablesMetadataParser {
    /**
     * Create a new instance of a {@link TablesMetadataParser}
     *
     * @return new {@link TablesMetadataParser} instance
     */
    public static TablesMetadataParser create() {
        return new TablesMetadataParser();
    }

    private TablesMetadataParser() {
    }

    /**
     * Parse a list of tables in JsonArray format to a list of {@link TableMetadata}
     *
     * @param tablesAsJson JSON array of table metadata
     * @return list of {@link TableMetadata}
     */
    public List<TableMetadata> parse(final JsonArray tablesAsJson) {
        return parseTables(tablesAsJson);
    }

    private List<TableMetadata> parseTables(final JsonArray jsonArray) {
        final List<TableMetadata> tables = new ArrayList<>();
        for (final JsonObject table : jsonArray.getValuesAs(JsonObject.class)) {
            final String tableName = table.getString(TABLE_NAME_KEY, "");
            final String tableAdapterNotes = readAdapterNotes(table);
            final String tableComment = table.getString(TABLE_COMMENT_KEY, "");
            final List<ColumnMetadata> columns = new ArrayList<>();
            final JsonArray columnsAsJson = requireJsonArray(table, TABLE_COLUMNS_KEY,
                    describeTable(tableName));
            int columnIndex = 0;
            for (final JsonObject column : columnsAsJson.getValuesAs(JsonObject.class)) {
                columns.add(parseColumnMetadata(column, tableName, columnIndex));
                columnIndex++;
            }
            tables.add(new TableMetadata(tableName, tableAdapterNotes, columns, tableComment));
        }
        return tables;
    }

    private ColumnMetadata parseColumnMetadata(final JsonObject column, final String tableName, final int columnIndex) {
        final String columnDescription = describeColumn(tableName, columnIndex, column.getString(TABLE_NAME_KEY, null));
        final String columnName = requireString(column, TABLE_NAME_KEY, columnDescription);
        final String adapterNotes = readAdapterNotes(column);
        final String comment = column.getString(TABLE_COMMENT_KEY, "");
        final String defaultValue = column.getString("default", "");
        final boolean isNullable = applyBooleanValue(column, "isNullable", true);
        final boolean isIdentity = applyBooleanValue(column, "isIdentity", false);
        final JsonObject dataType = requireJsonObject(column, DATA_TYPE, columnDescription);
        final DataType type = getDataType(dataType, columnDescription);
        return ColumnMetadata.builder().name(columnName).adapterNotes(adapterNotes).type(type).nullable(isNullable)
                .identity(isIdentity).defaultValue(defaultValue).comment(comment).build();
    }

    private JsonArray requireJsonArray(final JsonObject root, final String key, final String context) {
        final JsonArray jsonArray = root.getJsonArray(key);
        if (jsonArray == null) {
            throw missingRequiredField(key, context);
        }
        return jsonArray;
    }

    private JsonObject requireJsonObject(final JsonObject root, final String key, final String context) {
        final JsonObject jsonObject = root.getJsonObject(key);
        if (jsonObject == null) {
            throw missingRequiredField(key, context);
        }
        return jsonObject;
    }

    private String requireString(final JsonObject root, final String key, final String context) {
        final String value = root.getString(key, null);
        if (value == null) {
            throw missingRequiredField(key, context);
        }
        return value;
    }

    private RequestParserException missingRequiredField(final String key, final String context) {
        return new RequestParserException(ExaError.messageBuilder("E-VSCOMJAVA-44")
                .message("Failed to parse {{context}} because mandatory field {{field}} is missing.")
                .parameter("context", context)
                .parameter("field", key)
                .toString());
    }

    private String describeTable(final String tableName) {
        return tableName.isEmpty() ? "table metadata" : "table '" + tableName + "'";
    }

    private String describeColumn(final String tableName, final int columnIndex, final String columnName) {
        final StringBuilder description = new StringBuilder("column ");
        if (columnName != null) {
            description.append('\'').append(columnName).append('\'');
        } else {
            description.append('#').append(columnIndex);
        }
        if ((tableName != null) && !tableName.isEmpty()) {
            description.append(" of table '").append(tableName).append('\'');
        }
        return description.toString();
    }

    private String readAdapterNotes(final JsonObject root) {
        if (root.containsKey("adapterNotes")) {
            final JsonValue notes = root.get("adapterNotes");
            return getAdapterNotesString(notes);
        }
        return "";
    }

    private String getAdapterNotesString(final JsonValue notes) {
        if (notes.getValueType() == JsonValue.ValueType.STRING) {
            return ((JsonString) notes).getString();
        } else {
            return notes.toString();
        }
    }

    private boolean applyBooleanValue(final JsonObject column, final String booleanName,
            final boolean defaultValue) {
        if (column.containsKey(booleanName) && !column.isNull(booleanName)) {
            return column.getBoolean(booleanName);
        }
        return defaultValue;
    }

    private static DataType.ExaCharset charSetFromString(final String charset) {
        if (charset.equals("UTF8")) {
            return DataType.ExaCharset.UTF8;
        } else if (charset.equals("ASCII")) {
            return DataType.ExaCharset.ASCII;
        } else {
            throw new RequestParserException(ExaError.messageBuilder("E-VSCOMJAVA-19") //
                    .message("Unsupported charset encountered: {{charset}}.") //
                    .parameter("charset", charset).toString());
        }
    }

    private DataType getHashtypeDataType(final JsonObject dataType) {
        final int bytesize = dataType.getInt("bytesize");
        return DataType.createHashtype(bytesize);
    }

    private DataType getGeometryDataType(final JsonObject dataType) {
        final int srid = dataType.getInt("srid");
        return DataType.createGeometry(srid);
    }

    private DataType getIntervalDataType(final JsonObject dataType) {
        final int precision = dataType.getInt("precision", 2);
        final DataType.IntervalType intervalType = intervalTypeFromString(dataType.getString("fromTo"));
        if (intervalType == DataType.IntervalType.DAY_TO_SECOND) {
            final int fraction = dataType.getInt("fraction", 3);
            return DataType.createIntervalDaySecond(precision, fraction);
        } else {
            return DataType.createIntervalYearMonth(precision);
        }
    }

    private DataType getTimestampDataType(final JsonObject dataType) {
        final boolean withLocalTimezone = dataType.getBoolean("withLocalTimeZone", false);
        final int precision = dataType.getInt(SchemaMetadataJsonConverter.TIMESTAMP_PRECISION_KEY,
                DataTypeParser.DEFAULT_TIMESTAMP_PRECISION);
        return DataType.createTimestamp(withLocalTimezone, precision);
    }

    private DataType getDateDataType() {
        return DataType.createDate();
    }

    private DataType getBooleanDataType() {
        return DataType.createBool();
    }

    private DataType getCharDataType(final JsonObject dataType) {
        final String charSet = dataType.getString("characterSet", "UTF8");
        return DataType.createChar(dataType.getInt("size"), charSetFromString(charSet));
    }

    private DataType getVarcharDataType(final JsonObject dataType) {
        final String charSet = dataType.getString("characterSet", "UTF8");
        return DataType.createVarChar(dataType.getInt("size"), charSetFromString(charSet));
    }

    private DataType getDoubleDataType() {
        return DataType.createDouble();
    }

    private DataType getDecimalDataType(final JsonObject dataType) {
        return DataType.createDecimal(dataType.getInt("precision"), dataType.getInt("scale"));
    }

    private static DataType.IntervalType intervalTypeFromString(final String intervalType) {
        if (intervalType.equals("DAY TO SECONDS")) {
            return DataType.IntervalType.DAY_TO_SECOND;
        } else if (intervalType.equals("YEAR TO MONTH")) {
            return DataType.IntervalType.YEAR_TO_MONTH;
        } else {
            throw new RequestParserException(ExaError.messageBuilder("E-VSCOMJAVA-20") //
                    .message("Unsupported interval data type encountered: {{intervalType}}.") //
                    .parameter("intervalType", intervalType).toString());
        }
    }

    private DataType getDataType(final JsonObject dataType, final String columnDescription) {
        final String typeName = requireString(dataType, "type", columnDescription + " data type").toUpperCase(Locale.ROOT);
        switch (typeName) {
            case "DECIMAL":
                return getDecimalDataType(dataType);
            case "DOUBLE":
                return getDoubleDataType();
            case "VARCHAR":
                return getVarcharDataType(dataType);
            case "CHAR":
                return getCharDataType(dataType);
            case "BOOLEAN":
                return getBooleanDataType();
            case "DATE":
                return getDateDataType();
            case "TIMESTAMP":
                return getTimestampDataType(dataType);
            case "INTERVAL":
                return getIntervalDataType(dataType);
            case "GEOMETRY":
                return getGeometryDataType(dataType);
            case "HASHTYPE":
                return getHashtypeDataType(dataType);
            default:
                throw new RequestParserException(ExaError.messageBuilder("E-VSCOMJAVA-18")
                        .message("Unsupported data type encountered: {{typeName}}.") //
                        .parameter("typeName", typeName).toString());
        }
    }
}
