package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Node in a graph representing a SQL query.
 */
public abstract class SqlNode {
    private SqlNode parent;

    public abstract SqlNodeType getType();

    public void setParent(final SqlNode parent) {
        this.parent = parent;
    }

    public SqlNode getParent() {
        return this.parent;
    }

    public boolean hasParent() {
        return (this.parent != null);
    }
    
    /**
     * See {@link SqlNodeVisitor}
     * @param visitor The visitor object on which the appropriate visit(sqlNode) method is called
     * @param <R> generic SqlNodeVisitor type
     *
     * @return visited object
     *
     * @throws AdapterException can be thrown
     */
    public abstract <R> R accept(SqlNodeVisitor<R> visitor) throws AdapterException;

    /**
     * @return A SQL representation of the current graph, using EXASOL SQL syntax. It is called "SIMPLE" because it is not guaranteed to be 100 % correct SQL (e.g. might be ambiguous).
     */
    abstract String toSimpleSql();
}
