package com.exasol.adapter.json;

import java.util.*;

import javax.json.*;
import javax.json.JsonValue.ValueType;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.*;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.DataType.IntervalType;
import com.exasol.adapter.request.*;
import com.exasol.adapter.sql.*;
import com.exasol.utils.JsonHelper;

public class RequestJsonParser {
    private static final String PROPERTIES = "properties";
    private static final String INVOLVED_TABLES = "involvedTables";
    private static final String DATA_TYPE = "dataType";

    private List<TableMetadata> involvedTablesMetadata;

    public AdapterRequest parseRequest(final String json) throws AdapterException {
        final JsonObject root = JsonHelper.getJsonObject(json);
        final String requestType = root.getString("type", "");
        final SchemaMetadataInfo meta = parseMetadataInfo(root);
        switch (requestType) {
        case "createVirtualSchema":
            return createVirtualSchema(meta);
        case "dropVirtualSchema":
            return dropVirtualSchema(meta);
        case "refresh":
            return refresh(root, meta);
        case "setProperties":
            return setProperties(root, meta);
        case "getCapabilities":
            return getCapabilities(meta);
        case "pushdown":
            return pushdown(root, meta);
        default:
            throw new UnsupportedOperationException("Request Type not supported: " + requestType);
        }
    }

    private SchemaMetadataInfo parseMetadataInfo(final JsonObject root) {
        final JsonObject meta = root.getJsonObject("schemaMetadataInfo");
        if (meta == null) {
            return null;
        }
        final String schemaName = meta.getString("name");
        final String schemaAdapterNotes = readAdapterNotes(meta);
        final Map<String, String> properties = new HashMap<>();
        if (meta.getJsonObject(PROPERTIES) != null) {
            for (final Map.Entry<String, JsonValue> entry : meta.getJsonObject(PROPERTIES).entrySet()) {
                final String key = entry.getKey();
                setPropertyValue(meta, properties, key);
            }
        }
        return new SchemaMetadataInfo(schemaName, schemaAdapterNotes, properties);
    }

    private AdapterRequest createVirtualSchema(final SchemaMetadataInfo meta) {
        return new CreateVirtualSchemaRequest(meta);
    }

    private AdapterRequest dropVirtualSchema(final SchemaMetadataInfo meta) {
        return new DropVirtualSchemaRequest(meta);
    }

    private AdapterRequest refresh(final JsonObject root, final SchemaMetadataInfo meta) throws AdapterException {
        if (root.containsKey("requestedTables")) {
            final List<String> tables = getListOfTablesToBeRefreshed(root);
            return new RefreshRequest(meta, tables);
        } else {
            return new RefreshRequest(meta);
        }
    }

    private List<String> getListOfTablesToBeRefreshed(final JsonObject root) {
        final List<String> tables = new ArrayList<>();
        for (final JsonString table : root.getJsonArray("requestedTables").getValuesAs(JsonString.class)) {
            tables.add(table.getString());
        }
        return tables;
    }

    private AdapterRequest setProperties(final JsonObject root, final SchemaMetadataInfo meta) {
        validateJsonValueType(root, PROPERTIES, ValueType.OBJECT);
        final Map<String, String> properties = new HashMap<>();
        for (final Map.Entry<String, JsonValue> entry : root.getJsonObject(PROPERTIES).entrySet()) {
            setProperty(root, properties, entry);
        }
        return new SetPropertiesRequest(meta, properties);
    }

    private void validateJsonValueType(final JsonObject node, final String jsonKey, final ValueType type) {
        assert node.containsKey(jsonKey) && (node.get(jsonKey).getValueType() == type);
    }

    private void setProperty(final JsonObject root, final Map<String, String> properties,
            final Map.Entry<String, JsonValue> entry) {
        final String key = entry.getKey();
        if (isPropertyUnset(root, key)) {
            deleteProperty(properties, key);
        } else {
            setPropertyValue(root, properties, key);
        }
    }

    private void deleteProperty(final Map<String, String> properties, final String key) {
        properties.put(key.toUpperCase(), null);
    }

    private boolean isPropertyUnset(final JsonObject root, final String key) {
        return root.getJsonObject(PROPERTIES).isNull(key);
    }

    private void setPropertyValue(final JsonObject root, final Map<String, String> properties, final String key) {
        properties.put(key.toUpperCase(), root.getJsonObject(PROPERTIES).getString(key));
    }

    private AdapterRequest getCapabilities(final SchemaMetadataInfo meta) {
        return new GetCapabilitiesRequest(meta);
    }

    private AdapterRequest pushdown(final JsonObject root, final SchemaMetadataInfo meta) throws MetadataException {
        validateJsonValueType(root, INVOLVED_TABLES, ValueType.ARRAY);
        this.involvedTablesMetadata = parseInvolvedTableMetadata(root.getJsonArray(INVOLVED_TABLES));
        final JsonObject pushdownExp = getPushDownExpression(root);
        final SqlNode select = PushdownSqlParser.create().parseExpression(pushdownExp);
        assert select.getType() == SqlNodeType.SELECT;
        return new PushdownRequest(meta, (SqlStatementSelect) select, this.involvedTablesMetadata);
    }

    private JsonObject getPushDownExpression(final JsonObject root) {
        final JsonObject pushdownExp;
        if (root.containsKey("pushdownRequest")) {
            pushdownExp = root.getJsonObject("pushdownRequest");
        } else {
            throw new IllegalArgumentException(
                    "Push-down statement missing in adapter request element '/pushdownRequest'.");
        }
        return pushdownExp;
    }

    private List<TableMetadata> parseInvolvedTableMetadata(final JsonArray involvedTables) throws MetadataException {
        final List<TableMetadata> tables = new ArrayList<>();
        for (final JsonObject table : involvedTables.getValuesAs(JsonObject.class)) {
            final String tableName = table.getString("name", "");
            final String tableAdapterNotes = readAdapterNotes(table);
            final String tableComment = table.getString("comment", "");
            final List<ColumnMetadata> columns = new ArrayList<>();
            for (final JsonObject column : table.getJsonArray("columns").getValuesAs(JsonObject.class)) {
                columns.add(parseColumnMetadata(column));
            }
            tables.add(new TableMetadata(tableName, tableAdapterNotes, columns, tableComment));
        }
        return tables;
    }

    private ColumnMetadata parseColumnMetadata(final JsonObject column) throws MetadataException {
        final String columnName = column.getString("name");
        final String adapterNotes = readAdapterNotes(column);
        final String comment = column.getString("comment", "");
        final String defaultValue = column.getString("default", "");
        boolean isNullable = true;
        if (column.containsKey("isNullable")) {
            isNullable = column.getBoolean("isNullable");
        }
        boolean isIdentity = true;
        if (column.containsKey("isIdentity")) {
            isIdentity = column.getBoolean("isIdentity");
        }
        final JsonObject dataType = column.getJsonObject(DATA_TYPE);
        final DataType type = getDataType(dataType);
        return new ColumnMetadata(columnName, adapterNotes, type, isNullable, isIdentity, defaultValue, comment);
    }

    private DataType getDataType(final JsonObject dataType) throws MetadataException {
        final String typeName = dataType.getString("type").toUpperCase();
        DataType type = null;
        if (typeName.equals("DECIMAL")) {
            type = DataType.createDecimal(dataType.getInt("precision"), dataType.getInt("scale"));
        } else if (typeName.equals("DOUBLE")) {
            type = DataType.createDouble();
        } else if (typeName.equals("VARCHAR")) {
            final String charSet = dataType.getString("characterSet", "UTF8");
            type = DataType.createVarChar(dataType.getInt("size"), charSetFromString(charSet));
        } else if (typeName.equals("CHAR")) {
            final String charSet = dataType.getString("characterSet", "UTF8");
            type = DataType.createChar(dataType.getInt("size"), charSetFromString(charSet));
        } else if (typeName.equals("BOOLEAN")) {
            type = DataType.createBool();
        } else if (typeName.equals("DATE")) {
            type = DataType.createDate();
        } else if (typeName.equals("TIMESTAMP")) {
            final boolean withLocalTimezone = dataType.getBoolean("withLocalTimeZone", false);
            type = DataType.createTimestamp(withLocalTimezone);
        } else if (typeName.equals("INTERVAL")) {
            final int precision = dataType.getInt("precision", 2); // has a default in EXASOL
            final IntervalType intervalType = intervalTypeFromString(dataType.getString("fromTo"));
            if (intervalType == IntervalType.DAY_TO_SECOND) {
                final int fraction = dataType.getInt("fraction", 3); // has a default in EXASOL
                type = DataType.createIntervalDaySecond(precision, fraction);
            } else {
                assert intervalType == IntervalType.YEAR_TO_MONTH;
                type = DataType.createIntervalYearMonth(precision);
            }
        } else if (typeName.equals("GEOMETRY")) {
            final int srid = dataType.getInt("srid");
            type = DataType.createGeometry(srid);
        } else {
            throw new MetadataException("Unsupported data type encountered: " + typeName);
        }
        return type;
    }

    private static IntervalType intervalTypeFromString(final String intervalType) throws MetadataException {
        if (intervalType.equals("DAY TO SECONDS")) {
            return IntervalType.DAY_TO_SECOND;
        } else if (intervalType.equals("YEAR TO MONTH")) {
            return IntervalType.YEAR_TO_MONTH;
        } else {
            throw new MetadataException("Unsupported interval data type encountered: " + intervalType);
        }
    }

    private static ExaCharset charSetFromString(final String charset) throws MetadataException {
        if (charset.equals("UTF8")) {
            return ExaCharset.UTF8;
        } else if (charset.equals("ASCII")) {
            return ExaCharset.ASCII;
        } else {
            throw new MetadataException("Unsupported charset encountered: " + charset);
        }
    }

    private static String readAdapterNotes(final JsonObject root) {
        if (root.containsKey("adapterNotes")) {
            final JsonValue notes = root.get("adapterNotes");
            if (notes.getValueType() == ValueType.STRING) {
                // Return unquoted string
                return ((JsonString) notes).getString();
            } else {
                return notes.toString();
            }
        }
        return "";
    }

}
