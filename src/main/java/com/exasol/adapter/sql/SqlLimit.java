package com.exasol.adapter.sql;


import com.exasol.adapter.AdapterException;

public class SqlLimit extends SqlNode {

    private int limit;
    private int offset;

    public SqlLimit(int limit) {
        this(limit, 0);
    }

    public SqlLimit(int limit, int offset) {
        if (offset < 0 || limit <0) {
            throw new IllegalArgumentException("SqlLimit constructor expects offset and limit values to be greater than zero");
        }

        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public boolean hasOffset() {
        return offset != 0;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toSimpleSql() {
        String offsetSql = "";
        if (offset != 0) {
            offsetSql = " OFFSET " + offset;
        }
        return "LIMIT " + limit + offsetSql;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LIMIT;
    }

    @Override
    public <R> R accept(SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

}
