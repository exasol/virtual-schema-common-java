package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.DataTypeProperty.*;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.errorreporting.ExaError;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Starting with major version 8 Exasol database uses the capabilities reported by each virtual schema to provide select
 * list data types for each push down request. Based on this information the JDBC virtual schemas no longer need to
 * infer the data types of the result set by inspecting its values. Instead the JDBC virtual schemas can now use the
 * information provided by the database.
 *
 * <p>
 * Class {@link DataTypeParser} parses the data types from json.
 * </p>
 */
public class DataTypeParser {

    /**
     * @return new instance of {@link DataTypeParser}
     */
    public static DataTypeParser create() {
        return new DataTypeParser();
    }

    private DataTypeParser() {
    }

    /**
     * @param jsonArray {@link JsonArray} containing the data types to parse
     * @return list of parsed data types
     */
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
            return DataType.createGeometry(SPATIAL_REFERENCE_ID.get(entry, 0));
        case "HASHTYPE":
            return DataType.createHashtype(BYTESIZE.get(entry, 16));
        case "INTERVAL":
            return createInterval(entry);
        case "UNSUPPORTED": // falling through intentionally
        default:
            throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-37") //
                    .message("Unsupported datatype {{datatype}}.", TYPE.get(entry)) //
                    .ticketMitigation().toString());
        }
    }

    private DataType createInterval(final JsonObject entry) {
        final int precision = PRECISION.get(entry, 2);
        switch (FROM_TO.get(entry)) {
        case YEAR_TO_MONTH:
            return DataType.createIntervalYearMonth(precision);
        case DAY_TO_SECOND:
            // falling through intentionally
            // FROM_TO.get() ensures no other values
        default:
            return DataType.createIntervalDaySecond(precision, FRACTION.get(entry, 3));
        }
    }

    /**
     * Signal an error during parsing data types from json.
     */
    public static class DataTypeParserException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        DataTypeParserException(final String message) {
            super(message);
        }

        DataTypeParserException(final String message, final Exception exception) {
            super(message, exception);
        }
    }
}
