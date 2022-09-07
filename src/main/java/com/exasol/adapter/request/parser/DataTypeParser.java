package com.exasol.adapter.request.parser;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.request.parser.DataTypeProperty.*;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class DataTypeParser {

    private static StringProperty TYPE = new StringProperty("type");
    private static IntProperty PRECISION = new IntProperty("precision");
    private static IntProperty SCALE = new IntProperty("scale");
    private static IntProperty SIZE = new IntProperty("size");
    private static CharsetProperty CHARSET = new CharsetProperty("characterSet");
    private static IntProperty FRACTION = new IntProperty("fraction"); // TODO: verify!
    private static BooleanProperty WITH_LOCAL_TIMEZONE = new BooleanProperty("withlocaltimezone"); // TODO: verify!

    public static DataTypeParser create() {
        return new DataTypeParser();
    }

    private DataTypeParser() {
    }

    public List<DataType> parse(final JsonArray jsonArray) {
        return jsonArray.getValuesAs(JsonObject.class).stream() //
                .map(this::datatype) //
                .collect(Collectors.toList());
    }

    private DataType datatype(final JsonObject entry) {
        switch (TYPE.get(entry)) {
        case "DECIMAL":
            return DataType.createDecimal(PRECISION.get(entry), SCALE.get(entry));
        case "DOUBLE":
            return DataType.createDouble();
        case "VARCHAR":
            return DataType.createVarChar(SIZE.get(entry), CHARSET.get(entry));
        case "CHAR":
            return DataType.createChar(SIZE.get(entry), CHARSET.get(entry));
        case "DATE":
            return DataType.createDate();
        case "TIMESTAMP":
            return DataType.createTimestamp(WITH_LOCAL_TIMEZONE.get(entry));
        case "BOOLEAN":
            return DataType.createBool();
        case "GEOMETRY":
            return DataType.createGeometry(SCALE.get(entry));
        case "INTERVAL":
            return DataType.createIntervalDaySecond(PRECISION.get(entry), FRACTION.get(entry));
        case "UNSUPPORTED":
            // fall through
        default:
            return null;
        }
    }

}
