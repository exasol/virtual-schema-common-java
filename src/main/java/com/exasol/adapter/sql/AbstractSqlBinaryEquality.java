package com.exasol.adapter.sql;

public abstract class AbstractSqlBinaryEquality extends SqlPredicate {
    private final SqlNode left;
    private final SqlNode right;

    public AbstractSqlBinaryEquality(final Predicate function, final SqlNode left,
          final SqlNode right) {
        super(function);
        this.left = left;
        this.right = right;
        if (this.left != null) {
            this.left.setParent(this);
        }
        if (this.right != null) {
            this.right.setParent(this);
        }
    }

    public SqlNode getLeft() {
        return this.left;
    }

    public SqlNode getRight() {
        return this.right;
    }

    public String toSimpleSql(final String equalityExpression) {
        return this.left.toSimpleSql() + equalityExpression + this.right.toSimpleSql();
    }
}
