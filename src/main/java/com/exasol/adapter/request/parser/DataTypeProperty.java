package com.exasol.adapter.request.parser;

import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.request.parser.DataTypeParser.DataTypeParserException;
import com.exasol.errorreporting.ExaError;

import jakarta.json.JsonObject;

class DataTypeProperty<T> {

    static StringProperty TYPE = new StringProperty("type");
    static IntProperty PRECISION = new IntProperty("precision");
    static IntProperty SCALE = new IntProperty("scale");
    static IntProperty SIZE = new IntProperty("size");
    static CharsetProperty CHARSET = new CharsetProperty("characterSet");
    // These can only be verified by using exasol-virtual-schema
    // as most other virtual schemas do not support data types using any of these properties

    // TODO: verify with data type TIMESTAMP!
    static BooleanProperty WITH_LOCAL_TIMEZONE = new BooleanProperty("withLocalTimeZone");
    static IntProperty FRACTION = new IntProperty("fraction"); // TODO: verify with data type INTERVAL!
    static IntProperty BYTESIZE = new IntProperty("byteSize"); // TODO: verify with data type HASHTYPE!

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

    static class CharsetProperty extends DataTypeProperty<ExaCharset> {
        CharsetProperty(final String key) {
            super(key, CharsetProperty::getCharset);
        }

        private static ExaCharset getCharset(final JsonObject json, final String key) {
            switch (json.getString(key)) {
            case "ASCII":
                return ExaCharset.ASCII;
            case "UTF8":
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