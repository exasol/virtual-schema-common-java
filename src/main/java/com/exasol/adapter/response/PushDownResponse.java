package com.exasol.adapter.response;

import com.exasol.adapter.response.converter.ResponseException;

/**
 * This class is an abstract representation of a response created by a Virtual Schema Adapter as result of a request to
 * push down.
 */
public final class PushDownResponse {
    private final String pushDownSql;

    private PushDownResponse(final Builder builder) {
        this.pushDownSql = builder.pushDownSql;
    }

    /**
     * Get a {@link PushDownResponse} builder
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get push down SQL
     *
     * @return push down SQL string
     */
    public String getPushDownSql() {
        return this.pushDownSql;
    }

    /**
     * Builder for {@link PushDownResponse}
     */
    public static class Builder {
        private String pushDownSql;

        /**
         * Add push down SQL
         *
         * @param pushDownSql SQL string
         * @return builder instance for fluent programming
         */
        public Builder pushDownSql(final String pushDownSql) {
            this.pushDownSql = pushDownSql;
            return this;
        }

        /**
         * Create new {@link PushDownResponse} instance
         *
         * @return new {@link PushDownResponse} instance
         */
        public PushDownResponse build() {
            validate(this.pushDownSql);
            return new PushDownResponse(this);
        }

        private void validate(final String pushDownSql) {
            if (pushDownSql == null) {
                throw new ResponseException("Push down SQL string should be not null. Please, add push down SQL string " //
                        + "using 'pushDownSql(yourPushDownSqlString)' method of this builder before you build.");
            }
        }
    }
}
