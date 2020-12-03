package com.exasol.adapter.sql;

import java.util.List;

import com.exasol.adapter.AdapterException;

public final class SqlSelectList extends SqlExpressionList {
    private final SqlSelectListType type;

    private SqlSelectList(final SqlSelectListType type, final List<SqlNode> selectList) {
        super(selectList);
        this.type = type;
    }

    /**
     * Creates a SqlSelectList that uses an arbitrary value. See {@link SqlSelectListType#ANY_VALUE}.
     *
     * @return the new SqlSelectList.
     */
    public static SqlSelectList createAnyValueSelectList() {
        return new SqlSelectList(SqlSelectListType.ANY_VALUE, null);
    }

    /**
     * Creates a regular SqlSelectList. See {@link SqlSelectListType#REGULAR}.
     *
     * @param selectList The selectList needs at least one element.
     * @return the new SqlSelectList.
     */
    public static SqlSelectList createRegularSelectList(final List<SqlNode> selectList) {
        if ((selectList == null) || selectList.isEmpty()) {
            throw new IllegalArgumentException(
                    "SqlFunctionAggregateGroupConcat constructor expects an argument." + "But the list is empty.");
        }
        return new SqlSelectList(SqlSelectListType.REGULAR, selectList);
    }

    public SqlSelectListType getSelectListType() {
        return this.type;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.SELECT_LIST;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}