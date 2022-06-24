package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;

/**
 * Node in a graph representing a SQL query.
 */
public abstract class SqlNode {
    private SqlNode parent;

    /**
     * Gets type.
     *
     * @return the type
     */
    public abstract SqlNodeType getType();

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public SqlNode getParent() {
        return this.parent;
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     */
    public void setParent(final SqlNode parent) {
        this.parent = parent;
    }

    /**
     * Has parent boolean.
     *
     * @return the boolean
     */
    public boolean hasParent() {
        return (this.parent != null);
    }

    /**
     * See {@link SqlNodeVisitor}
     *
     * @param <R>     generic SqlNodeVisitor type
     * @param visitor The visitor object on which the appropriate visit(sqlNode) method is called
     * @return visited object
     * @throws AdapterException can be thrown
     */
    public abstract <R> R accept(SqlNodeVisitor<R> visitor) throws AdapterException;
}