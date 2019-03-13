package com.exasol.adapter.response;

import com.exasol.adapter.response.converter.ResponseJsonConverter;

/**
 * Used in overloaded convert() method of {@link ResponseJsonConverter}
 */
public final class DropVirtualSchemaResponse {
    public static Builder builder() {
        return new Builder();
    }

    private DropVirtualSchemaResponse() {
        //intentionally left blank
    }

    /**
     * Builder for {@link DropVirtualSchemaResponse}
     */
    public static final class Builder {
        /**
         * Create new {@link DropVirtualSchemaResponse} instance
         *
         * @return new {@link DropVirtualSchemaResponse} instance
         */
        public DropVirtualSchemaResponse build() {
            return new DropVirtualSchemaResponse();
        }
    }
}
