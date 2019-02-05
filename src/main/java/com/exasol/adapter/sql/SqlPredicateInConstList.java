package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlPredicateInConstList extends SqlPredicate {
    // For <exp> IN (...) this stores <exp>
    private final SqlNode expression;
    // Arguments inside the brackets
    private final List<SqlNode> inArguments;
    
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
        return expression;
    }
    
    public List<SqlNode> getInArguments() {
        if (inArguments == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(inArguments);
        }
    }
    
    @Override
    public String toSimpleSql() {
        final List<String> argumentsSql = new ArrayList<>();
        for (final SqlNode node : inArguments) {
            argumentsSql.add(node.toSimpleSql());
        }
        return expression.toSimpleSql() + " IN (" + String.join(", ", argumentsSql) + ")";
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
