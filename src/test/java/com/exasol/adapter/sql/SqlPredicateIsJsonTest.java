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
class SqlPredicateIsJsonTest {
    private SqlPredicateIsJson sqlPredicateIsJson;
    @Mock
    private SqlNode expressionMock;

    @BeforeEach
    void setUp() {
        this.sqlPredicateIsJson = new SqlPredicateIsJson(this.expressionMock,
                AbstractSqlPredicateJson.TypeConstraints.OBJECT,
                AbstractSqlPredicateJson.KeyUniquenessConstraint.WITH_UNIQUE_KEYS);
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateIsJson.getType(), equalTo(SqlNodeType.PREDICATE_IS_JSON));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateIsJson> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateIsJson)).thenReturn(this.sqlPredicateIsJson);
        assertThat(this.sqlPredicateIsJson.accept(visitor), equalTo(this.sqlPredicateIsJson));
    }

    @Test
    void testGetExpression() {
        assertThat(this.sqlPredicateIsJson.getExpression(), equalTo(this.expressionMock));
    }

    @Test
    void testGetTypeConstraint() {
        assertThat(this.sqlPredicateIsJson.getTypeConstraint(), equalTo("OBJECT"));
    }

    @Test
    void testGetKeyUniquenessConstraint() {
        assertThat(this.sqlPredicateIsJson.getKeyUniquenessConstraint(), equalTo("WITH UNIQUE KEYS"));
    }
}