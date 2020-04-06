package com.exasol.adapter.sql;

/**
 * This class contains a common behavior for the {@link SqlNodeType#PREDICATE_IS_JSON} and
 * {@link SqlNodeType#PREDICATE_IS_NOT_JSON} predicates.
 */
public abstract class AbstractSqlPredicateJson extends SqlPredicate {
    protected final SqlNode expression;
    protected final SqlPredicateIsJson.TypeConstraints typeConstraint;
    protected final SqlPredicateIsJson.KeyUniquenessConstraint keyUniquenessConstraint;

    public AbstractSqlPredicateJson(final Predicate predicate, final SqlNode expression,
            final SqlPredicateIsJson.TypeConstraints typeConstraint,
            final SqlPredicateIsJson.KeyUniquenessConstraint keyUniquenessConstraint) {
        super(predicate);
        this.expression = expression;
        this.typeConstraint = typeConstraint;
        this.keyUniquenessConstraint = keyUniquenessConstraint;
    }

    public SqlNode getExpression() {
        return this.expression;
    }

    public String getTypeConstraint() {
        return this.typeConstraint.toString();
    }

    public String getKeyUniquenessConstraint() {
        return this.keyUniquenessConstraint.toString();
    }

    /**
     * A list of expected type constraints.
     */
    public enum TypeConstraints {
        VALUE, ARRAY, OBJECT, SCALAR
    }

    /**
     * A list of expected key uniqueness constraints.
     */
    public enum KeyUniquenessConstraint {
        WITH_UNIQUE_KEYS("WITH UNIQUE KEYS"), WITHOUT_UNIQUE_KEYS("WITHOUT UNIQUE KEYS");

        private final String value;

        KeyUniquenessConstraint(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static SqlPredicateIsJson.KeyUniquenessConstraint of(final String value) {
            final String formattedValue = String.join("_", value.split(" ")).toUpperCase();
            return SqlPredicateIsJson.KeyUniquenessConstraint.valueOf(formattedValue);
        }
    }
}
