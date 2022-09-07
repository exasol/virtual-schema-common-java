package com.exasol.adapter.request.parser;

import com.exasol.adapter.metadata.DataType.ExaCharset;

import jakarta.json.JsonObject;

class DataTypeProperty<T> {
    protected final String key;
    private final DataTypeProperty.JsonGetter<T> getter;

    DataTypeProperty(final String key, final DataTypeProperty.JsonGetter<T> getter) {
        this.key = key;
        this.getter = getter;
    }

    public T get(final JsonObject json) {
        return this.getter.apply(json, this.key);
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
                throw new RuntimeException();
            }
        }
    }

    static class BooleanProperty extends DataTypeProperty<Boolean> {
        BooleanProperty(final String key) {
            super(key, JsonObject::getBoolean);
        }
    }

}