package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

/**
 * {@code LIMIT} sql node.
 */
public class SqlLimit extends SqlNode {
    private int limit;
    private int offset;

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
                    .message("SqlLimit constructor expects offset and limit values to be greater than zero")
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
     * Sets limit.
     *
     * @param limit the limit
     */
    public void setLimit(final int limit) {
        this.limit = limit;
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
     * Sets offset.
     *
     * @param offset the offset
     */
    public void setOffset(final int offset) {
        this.offset = offset;
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