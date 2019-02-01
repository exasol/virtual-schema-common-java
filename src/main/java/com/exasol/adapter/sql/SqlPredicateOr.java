package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SqlPredicateOr extends SqlPredicate {

    private List<SqlNode> orPredicates;

    public SqlPredicateOr(List<SqlNode> orPredicates) {
        super(Predicate.OR);
        this.orPredicates  = orPredicates;
        if (this.orPredicates != null) {
            for (SqlNode node : this.orPredicates) {
                node.setParent(this);
            }
        }
    }

    public List<SqlNode> getOrPredicates() {
        if (orPredicates == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(orPredicates);
        }
    }

    @Override
    public String toSimpleSql() {
        List<String> operandsSql = new ArrayList<>();
        for (SqlNode node : orPredicates) {
            operandsSql.add(node.toSimpleSql());
        }
        return "(" + String.join(" OR ", operandsSql) + ")";
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.PREDICATE_OR;
    }

    @Override
    public <R> R accept(SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }

}
