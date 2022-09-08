package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.DataTypeProperty.*;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.errorreporting.ExaError;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class DataTypeParser {

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
        if (!entry.containsKey(TYPE.key)) {
            throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-40") //
                    .message("Unspecified datatype in {{json}}.", entry.toString()) //
                    .ticketMitigation().toString());
        }
        switch (TYPE.get(entry)) {
        case "DECIMAL":
            // should we accept at least if both precision *and* scale missing and use default value -1 for both?
            return DataType.createDecimal(PRECISION.get(entry), SCALE.get(entry));
        case "DOUBLE":
            return DataType.createDouble();
        case "VARCHAR":
            return DataType.createVarChar(SIZE.get(entry), CHARSET.get(entry, ExaCharset.UTF8));
        case "CHAR":
            return DataType.createChar(SIZE.get(entry), CHARSET.get(entry, ExaCharset.UTF8));
        case "DATE":
            return DataType.createDate();
        case "TIMESTAMP":
            return DataType.createTimestamp(WITH_LOCAL_TIMEZONE.get(entry, false));
        case "BOOLEAN":
            return DataType.createBool();
        case "GEOMETRY":
            return DataType.createGeometry(SCALE.get(entry, 0));
        case "HASHTYPE":
            return DataType.createHashtype(BYTESIZE.get(entry, 16));
        case "INTERVAL":
            return DataType.createIntervalDaySecond(PRECISION.get(entry, 2), FRACTION.get(entry, 3));
        case "UNSUPPORTED": // fall through
        default:
            throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-37") //
                    .message("Unsupported datatype {{datatype}}.", TYPE.get(entry)) //
                    .ticketMitigation().toString());
        }
    }

    public static class DataTypeParserException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public DataTypeParserException(final String message) {
            super(message);
        }
    }
}
