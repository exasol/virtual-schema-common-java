package com.exasol.adapter.sql;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.AdapterException;

/**
 * The type Sql predicate in const list.
 */
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
        this.inArguments = inArguments == null ? emptyList() : List.copyOf(inArguments);
        if (this.expression != null) {
            this.expression.setParent(this);
        }
        for (final SqlNode node : this.inArguments) {
            node.setParent(this);
        }
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public SqlNode getExpression() {
        return this.expression;
    }

    /**
     * Gets in arguments.
     *
     * @return the in arguments
     */
    public List<SqlNode> getInArguments() {
        return this.inArguments;
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_IN_CONSTLIST;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() {
        final List<SqlNode> children = new ArrayList<>();
        children.addAll(this.inArguments);
        children.add(this.expression);
        return children;
    }
}
