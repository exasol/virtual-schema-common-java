package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.Arrays;
import java.util.List;

/**
 * Not predicate.
 */
public class SqlPredicateNot extends SqlPredicate {
    private final SqlNode expression;

    /**
     * Instantiates a new Sql predicate not.
     *
     * @param expression the expression
     */
    public SqlPredicateNot(final SqlNode expression) {
        super(Predicate.NOT);
        this.expression = expression;
        if (this.expression != null) {
            this.expression.setParent(this);
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

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_NOT;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

    @Override
    public List<SqlNode> getChildren() { return Arrays.asList(this.expression); }
}