package com.exasol.adapter.response;

/**
 * This class is an abstract representation of a response
 * created by a Virtual Schema Adapter as result of a request to push down.
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
            return new PushDownResponse(this);
        }
    }
}
