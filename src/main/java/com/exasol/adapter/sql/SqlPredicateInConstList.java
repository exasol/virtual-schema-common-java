package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Collections;
import java.util.List;

public class SqlPredicateInConstList extends SqlPredicate {
    private final SqlNode expression;
    private final List<SqlNode> inArguments;

    /**
     * Create a new instance of {@link SqlPredicateInConstList}.
     *
     * @param expression  For &lt;exp&gt; IN (...) this stores &lt;exp&gt;
     * @param inArguments arguments inside the brackets
     */
    public SqlPredicateInConstList(final SqlNode expression, final List<SqlNode> inArguments) {
        super(Predicate.IN_CONSTLIST);
        this.expression = expression;
        this.inArguments = inArguments;
        if (this.expression != null) {
            this.expression.setParent(this);
        }
        if (this.inArguments != null) {
            for (final SqlNode node : this.inArguments) {
                node.setParent(this);
            }
        }
    }

    public SqlNode getExpression() {
        return this.expression;
    }

    public List<SqlNode> getInArguments() {
        if (this.inArguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(this.inArguments);
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IN_CONSTLIST;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}