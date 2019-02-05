package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlPredicateAnd extends SqlPredicate {
    private final List<SqlNode> andedPredicates;

    public SqlPredicateAnd(final List<SqlNode> andedPredicates) {
        super(Predicate.AND);
        this.andedPredicates = andedPredicates;
        if (this.andedPredicates != null) {
            for (final SqlNode node : this.andedPredicates) {
                node.setParent(this);
            }
        }
    }

    public List<SqlNode> getAndedPredicates() {
        if (andedPredicates == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(andedPredicates);
        }
    }

    @Override
    public String toSimpleSql() {
        final List<String> operandsSql = new ArrayList<>();
        for (final SqlNode node : andedPredicates) {
            operandsSql.add(node.toSimpleSql());
        }
        return "(" + String.join(" AND ", operandsSql) + ")";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_AND;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
