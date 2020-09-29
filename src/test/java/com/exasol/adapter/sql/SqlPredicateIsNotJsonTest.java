package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

@ExtendWith(MockitoExtension.class)
class SqlPredicateIsNotJsonTest {
    private SqlPredicateIsNotJson sqlPredicateIsNotJson;
    @Mock
    private SqlNode expressionMock;

    @BeforeEach
    void setUp() {
        this.sqlPredicateIsNotJson = new SqlPredicateIsNotJson(this.expressionMock,
                AbstractSqlPredicateJson.TypeConstraints.VALUE,
                AbstractSqlPredicateJson.KeyUniquenessConstraint.WITHOUT_UNIQUE_KEYS);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateIsNotJson.getType(), equalTo(SqlNodeType.PREDICATE_IS_NOT_JSON));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateIsNotJson> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateIsNotJson)).thenReturn(this.sqlPredicateIsNotJson);
        assertThat(this.sqlPredicateIsNotJson.accept(visitor), equalTo(this.sqlPredicateIsNotJson));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateIsNotJson.getExpression(), equalTo(this.expressionMock));
    }

    @Test
    void testGetTypeConstraint() {
        assertThat(this.sqlPredicateIsNotJson.getTypeConstraint(), equalTo("VALUE"));
    }

    @Test
    void testGetKeyUniquenessConstraint() {
        assertThat(this.sqlPredicateIsNotJson.getKeyUniquenessConstraint(), equalTo("WITHOUT UNIQUE KEYS"));
    }
}