package com.exasol.adapter.sql;

import java.util.List;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

/**
 * This class represents a select list.
 */
public final class SqlSelectList extends SqlExpressionList {
    private SqlSelectList(final List<SqlNode> selectList) {
        super(selectList);
    }

    /**
     * Creates a SqlSelectList that uses an arbitrary value.
     *
     * @return new SqlSelectList.
     */
    public static SqlSelectList createAnyValueSelectList() {
        return new SqlSelectList(null);
    }

    /**
     * Creates a regular SqlSelectList.
     *
     * @param selectList selectList needs at least one element.
     * @return new SqlSelectList.
     */
    public static SqlSelectList createRegularSelectList(final List<SqlNode> selectList) {
        if ((selectList == null) || selectList.isEmpty()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSCOMJAVA-28")
                    .message("Cannot create a select list, because the list of arguments is empty.").toString());
        }
        return new SqlSelectList(selectList);
    }

    /**
     * Check if this SELECT has an explicit columns list.
     *
     * @return true if has an explicit columns list
     */
    public boolean hasExplicitColumnsList() {
        return super.getExpressions() != null && !super.getExpressions().isEmpty();
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