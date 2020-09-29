package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateAndTest {
    private SqlPredicateAnd sqlPredicateAnd;
    private SqlPredicateAnd sqlPredicateAndWithEmptyList;
    private List<SqlNode> andedPredicates;

    @BeforeEach
    void setUp() {
        this.andedPredicates = new ArrayList<>();
        this.andedPredicates.add(new SqlLiteralString("value1"));
        this.andedPredicates.add(new SqlLiteralString("value2"));
        this.sqlPredicateAnd = new SqlPredicateAnd(this.andedPredicates);
        this.sqlPredicateAndWithEmptyList = new SqlPredicateAnd(null);
    }

    @Test
    void testGetAndedPredicates() {
        assertThat(this.sqlPredicateAnd.getAndedPredicates(), equalTo(this.andedPredicates));
    }

    @Test
    void testGetAndedPredicatesEmptyList() {
        assertThat(this.sqlPredicateAndWithEmptyList.getAndedPredicates(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateAnd.getType(), equalTo(SqlNodeType.PREDICATE_AND));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateAnd> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateAnd)).thenReturn(this.sqlPredicateAnd);
        assertThat(this.sqlPredicateAnd.accept(visitor), equalTo(this.sqlPredicateAnd));
    }
}