package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.RequestParserConstants.*;

import java.util.ArrayList;
import java.util.List;

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
            for (final JsonObject column : table.getJsonArray(TABLE_COLUMNS_KEY).getValuesAs(JsonObject.class)) {
                columns.add(parseColumnMetadata(column));
            }
            tables.add(new TableMetadata(tableName, tableAdapterNotes, columns, tableComment));
        }
        return tables;
    }

    private ColumnMetadata parseColumnMetadata(final JsonObject column) {
        final String columnName = column.getString(TABLE_NAME_KEY);
        final String adapterNotes = readAdapterNotes(column);
        final String comment = column.getString(TABLE_COMMENT_KEY, "");
        final String defaultValue = column.getString("default", "");
        final boolean isNullable = applyBooleanValue(column, "isNullable");
        final boolean isIdentity = applyBooleanValue(column, "isIdentity");
        final JsonObject dataType = column.getJsonObject(DATA_TYPE);
        final DataType type = getDataType(dataType);
        return ColumnMetadata.builder().name(columnName).adapterNotes(adapterNotes).type(type).nullable(isNullable)
                .identity(isIdentity).defaultValue(defaultValue).comment(comment).build();
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

    private boolean applyBooleanValue(final JsonObject column, final String bolleanName) {
        if (column.containsKey(bolleanName)) {
            return column.getBoolean(bolleanName);
        }
        return true;
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

    private DataType getDataType(final JsonObject dataType) {
        final String typeName = dataType.getString("type").toUpperCase();
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
