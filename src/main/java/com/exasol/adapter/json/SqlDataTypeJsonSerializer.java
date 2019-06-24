package com.exasol.adapter.json;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.DataType.ExaDataType;
import com.exasol.adapter.metadata.DataType.IntervalType;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public final class SqlDataTypeJsonSerializer {
    private SqlDataTypeJsonSerializer() {
        // Intentionally left blank.
    }

    public static JsonObjectBuilder serialize(final DataType dataType) {
        final JsonObjectBuilder root = Json.createObjectBuilder().add("type",
                exaTypeAsString(dataType.getExaDataType()));

        switch (dataType.getExaDataType()) {
        case UNSUPPORTED:
            throw new IllegalArgumentException("Unsupported Data Type, should never happen");
        case DECIMAL:
            root.add("precision", dataType.getPrecision());
            root.add("scale", dataType.getScale());
            break;
        case VARCHAR: // falling through intentionally
        case CHAR:
            root.add("size", dataType.getSize());
            root.add("characterSet", exaCharSetAsString(dataType.getCharset()));
            break;
        case TIMESTAMP:
            root.add("withLocalTimeZone", dataType.isWithLocalTimezone());
            break;
        case GEOMETRY:
            root.add("srid", dataType.getGeometrySrid());
            break;
        case INTERVAL:
            addIntervalToRoot(dataType, root);
            break;
        case DOUBLE: // falling through intentionally
        case DATE:
        case BOOLEAN:
            break;
        default:
            throw new IllegalArgumentException("Unexpected Data Type: " + dataType.getExaDataType());
        }

        return root;
    }

    private static void addIntervalToRoot(final DataType dataType, final JsonObjectBuilder root) {
        root.add("fromTo", intervalTypeAsString(dataType.getIntervalType()));
        root.add("precision", dataType.getPrecision());
        if (dataType.getIntervalType() == IntervalType.DAY_TO_SECOND) {
            root.add("fraction", dataType.getIntervalFraction());
        }
    }

    private static String exaTypeAsString(final ExaDataType dataType) {
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
        default:
            return "unknown";
        }
    }

    private static String exaCharSetAsString(final ExaCharset charset) {
        switch (charset) {
        case UTF8:
            return "UTF8";
        case ASCII:
            return "ASCII";
        default:
            throw new IllegalArgumentException("Unexpected Charset: " + charset);
        }
    }

    private static String intervalTypeAsString(final IntervalType intervalType) {
        switch (intervalType) {
        case DAY_TO_SECOND:
            return "DAY TO SECONDS";
        case YEAR_TO_MONTH:
            return "YEAR TO MONTH";
        default:
            throw new IllegalArgumentException("Unexpected IntervalType: " + intervalType);
        }
    }
}
