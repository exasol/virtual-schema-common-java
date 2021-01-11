package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

public class SqlLimit extends SqlNode {
    private int limit;
    private int offset;

    public SqlLimit(final int limit) {
        this(limit, 0);
    }

    public SqlLimit(final int limit, final int offset) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VS-COM-JAVA-27")
                    .message("SqlLimit constructor expects offset and limit values to be greater than zero")
                    .toString());
        }
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public boolean hasOffset() {
        return this.offset != 0;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
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