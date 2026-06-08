package com.exasol.adapter.sql;

import java.util.*;

/**
 * This class contains a common behavior for the {@link SqlNodeType#PREDICATE_IS_JSON} and
 * {@link SqlNodeType#PREDICATE_IS_NOT_JSON} predicates.
 */
public abstract class AbstractSqlPredicateJson extends SqlPredicate {
    /**
     * The Expression.
     */
    protected final SqlNode expression;
    /**
     * The Type constraint.
     */
    protected final TypeConstraints typeConstraint;
    /**
     * The Key uniqueness constraint.
     */
    protected final KeyUniquenessConstraint keyUniquenessConstraint;

    /**
     * Instantiates a new Abstract sql predicate json.
     *
     * @param predicate               the predicate
     * @param expression              the expression
     * @param typeConstraint          the type constraint
     * @param keyUniquenessConstraint the key uniqueness constraint
     */
    protected AbstractSqlPredicateJson(final Predicate predicate, final SqlNode expression, final TypeConstraints typeConstraint,
            final KeyUniquenessConstraint keyUniquenessConstraint) {
        super(predicate);
        this.expression = expression;
        this.typeConstraint = typeConstraint;
        this.keyUniquenessConstraint = keyUniquenessConstraint;
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
     * Gets type constraint.
     *
     * @return the type constraint
     */
    public String getTypeConstraint() {
        return this.typeConstraint.toString();
    }

    /**
     * Gets key uniqueness constraint.
     *
     * @return the key uniqueness constraint
     */
    public String getKeyUniquenessConstraint() {
        return this.keyUniquenessConstraint.toString();
    }

    /**
     * A list of expected type constraints.
     */
    public enum TypeConstraints {
        /**
         * Value type constraints.
         */
        VALUE,
        /**
         * Array type constraints.
         */
        ARRAY,
        /**
         * Object type constraints.
         */
        OBJECT,
        /**
         * Scalar type constraints.
         */
        SCALAR
    }

    /**
     * A list of expected key uniqueness constraints.
     */
    public enum KeyUniquenessConstraint {
        /**
         * The With unique keys.
         */
        WITH_UNIQUE_KEYS("WITH UNIQUE KEYS"),
        /**
         * The Without unique keys.
         */
        WITHOUT_UNIQUE_KEYS("WITHOUT UNIQUE KEYS");

        private final String value;

        KeyUniquenessConstraint(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Of sql predicate is json . key uniqueness constraint.
         *
         * @param value the value
         * @return the sql predicate is json . key uniqueness constraint
         */
        public static KeyUniquenessConstraint of(final String value) {
            final String formattedValue = String.join("_", value.split(" ")).toUpperCase(Locale.ROOT);
            return KeyUniquenessConstraint.valueOf(formattedValue);
        }
    }

    @Override
    public List<SqlNode> getChildren() {
        return Arrays.asList(this.expression);
    }
}
