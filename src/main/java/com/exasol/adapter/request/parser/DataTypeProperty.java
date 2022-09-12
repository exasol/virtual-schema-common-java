package com.exasol.adapter.request.parser;

import static com.exasol.adapter.metadata.converter.SchemaMetadataJsonConverter.*;

import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.metadata.DataType.IntervalType;
import com.exasol.adapter.request.parser.DataTypeParser.DataTypeParserException;
import com.exasol.errorreporting.ExaError;

import jakarta.json.JsonObject;

class DataTypeProperty<T> {

    static final StringProperty TYPE = new StringProperty(TYPE_KEY);
    static final IntProperty PRECISION = new IntProperty(PRECISION_KEY);
    static final IntProperty SCALE = new IntProperty(SCALE_KEY);
    static final IntProperty SIZE = new IntProperty(SIZE_KEY);
    static final CharsetProperty CHARSET = new CharsetProperty(CHARSET_KEY);
    static final BooleanProperty WITH_LOCAL_TIMEZONE = new BooleanProperty(WITH_LOCAL_TIMEZONE_KEY);
    static final IntProperty SPATIAL_REFERENCE_ID = new IntProperty(SPATIAL_REFERENCE_ID_KEY);
    static final IntProperty FRACTION = new IntProperty(FRACTION_KEY);
    static final IntProperty BYTESIZE = new IntProperty(BYTESIZE_KEY);
    static final IntervalTypeProperty FROM_TO = new IntervalTypeProperty(FROM_TO_KEY);

    protected final String key;
    private final DataTypeProperty.JsonGetter<T> getter;

    DataTypeProperty(final String key, final DataTypeProperty.JsonGetter<T> getter) {
        this.key = key;
        this.getter = getter;
    }

    public T get(final JsonObject json) {
        return get(json, null);
    }

    public T get(final JsonObject json, final T defaultValue) {
        if (json.containsKey(this.key)) {
            try {
                return this.getter.apply(json, this.key);
            } catch (final Exception exception) {
                throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-39") //
                        .message("Datatype {{datatype}}, property {{property}}: Illegal value {{value}}.", //
                                TYPE.get(json), this.key, json.get(this.key))
                        .ticketMitigation().toString(), exception);
            }
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-36") //
                .message("Datatype {{datatype}}: Missing property {{property}}.", TYPE.get(json), this.key) //
                .ticketMitigation().toString());
    }

    @FunctionalInterface
    interface JsonGetter<T> {
        T apply(JsonObject j, String s);
    }

    static class StringProperty extends DataTypeProperty<String> {
        StringProperty(final String key) {
            super(key, JsonObject::getString);
        }
    }

    static class IntProperty extends DataTypeProperty<Integer> {
        IntProperty(final String key) {
            super(key, JsonObject::getInt);
        }
    }

    static class IntervalTypeProperty extends DataTypeProperty<IntervalType> {
        IntervalTypeProperty(final String key) {
            super(key, IntervalTypeProperty::getValue);
        }

        private static IntervalType getValue(final JsonObject json, final String key) {
            switch (json.getString(key)) {
            case DAY_TO_SECONDS_VALUE:
                return IntervalType.DAY_TO_SECOND;
            case YEAR_TO_MONTH_VALUE:
                return IntervalType.YEAR_TO_MONTH;
            default:
                throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-41") //
                        .message("Datatype {{datatype}}: Unsupported interval type {{interval type}}.", TYPE.get(json),
                                json.getString(key)) //
                        .ticketMitigation().toString());
            }
        }
    }

    static class CharsetProperty extends DataTypeProperty<ExaCharset> {
        CharsetProperty(final String key) {
            super(key, CharsetProperty::getCharset);
        }

        private static ExaCharset getCharset(final JsonObject json, final String key) {
            switch (json.getString(key)) {
            case ASCII_VALUE:
                return ExaCharset.ASCII;
            case UTF8_VALUE:
                return ExaCharset.UTF8;
            default:
                throw new DataTypeParserException(ExaError.messageBuilder("E-VSCOMJAVA-38") //
                        .message("Datatype {{datatype}}: Unsupported charset {{charset}}.", TYPE.get(json),
                                json.getString(key)) //
                        .ticketMitigation().toString());
            }
        }
    }

    static class BooleanProperty extends DataTypeProperty<Boolean> {
        BooleanProperty(final String key) {
            super(key, JsonObject::getBoolean);
        }
    }

}