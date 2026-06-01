package com.exasol.adapter.sql;

import static java.util.Collections.emptyList;

import java.util.List;

import com.exasol.adapter.AdapterException;
import com.exasol.errorreporting.ExaError;

/**
 * {@code ORDER BY} clause.
 */
public class SqlOrderBy extends SqlNode {
    private final List<SqlNode> expressions;
    private final List<Boolean> isAsc;
    private final List<Boolean> nullsLast;

    /**
     * Instantiates a new Sql order by.
     *
     * @param expressions the expressions
     * @param isAsc       the is asc
     * @param nullsFirst  the nulls first
     */
    public SqlOrderBy(final List<SqlNode> expressions, final List<Boolean> isAsc, final List<Boolean> nullsFirst) {
        this.expressions = copyList(expressions);
        this.isAsc = copyList(isAsc);
        this.nullsLast = copyList(nullsFirst);
        validateListSizes();
        if (this.expressions != null) {
            for (final SqlNode node : this.expressions) {
                node.setParent(this);
            }
        }
    }

    private <T> List<T> copyList(final List<T> values) {
        return values == null ? emptyList() : List.copyOf(values);
    }

    private void validateListSizes() {
        final int expressionsSize = this.expressions.size();
        if (expressionsSize != this.isAsc.size() || expressionsSize != this.nullsLast.size()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("F-VSCOMJAVA-46")
                    .message("Can not create SqlOrderBy with an invalid format. The size of the three lists must be equal.")
                    .ticketMitigation().toString());
        }
    }

    /**
     * Gets expressions.
     *
     * @return the expressions
     */
    public List<SqlNode> getExpressions() {
        return this.expressions;
    }

    /**
     * Is ascending list.
     *
     * @return the list
     */
    public List<Boolean> isAscending() {
        return this.isAsc;
    }

    /**
     * Nulls last list.
     *
     * @return the list
     */
    public List<Boolean> nullsLast() {
        return this.nullsLast;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.ORDER_BY;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() {
        return getExpressions();
    }
}
