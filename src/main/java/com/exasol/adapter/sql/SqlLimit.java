package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

/**
 * {@code LIMIT} sql node.
 */
public class SqlLimit extends SqlNode {
    private final int limit;
    private final int offset;

    /**
     * Instantiates a new SQL limit.
     *
     * @param limit the limit
     */
    public SqlLimit(final int limit) {
        this(limit, 0);
    }

    /**
     * Instantiates a new SQL limit.
     *
     * @param limit  the limit
     * @param offset the offset
     */
    public SqlLimit(final int limit, final int offset) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-27")
                    .message("SqlLimit constructor expects offset and limit values to be greater than or equal to zero")
                    .toString());
        }
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * Gets limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * @deprecated `SqlLimit` is immutable. Create a new instance instead.
     *
     * @param limit ignored
     */
    @Deprecated(since = "18.0.2", forRemoval = true)
    public void setLimit(final int limit) {
        throw new UnsupportedOperationException(
                ExaError.messageBuilder("E-VSCOMJAVA-42").message("SqlLimit is immutable. Create a new instance instead.")
                        .toString());
    }

    /**
     * Gets offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * @deprecated `SqlLimit` is immutable. Create a new instance instead.
     *
     * @param offset ignored
     */
    @Deprecated(since = "18.0.2", forRemoval = true)
    public void setOffset(final int offset) {
        throw new UnsupportedOperationException(
                ExaError.messageBuilder("E-VSCOMJAVA-43").message("SqlLimit is immutable. Create a new instance instead.")
                        .toString());
    }

    /**
     * Has offset boolean.
     *
     * @return the boolean
     */
    public boolean hasOffset() {
        return this.offset != 0;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LIMIT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
