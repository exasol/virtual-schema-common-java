package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        this.sqlPredicateAndWithEmptyList = new SqlPredicateAnd(Collections.emptyList());
    }

    @Test
    void testGetAndedPredicates() {
        assertThat(this.sqlPredicateAnd.getAndedPredicates(), equalTo(this.andedPredicates));
    }

    @Test
    void testGetAndedPredicatesEmptyList() {
        assertThat(this.sqlPredicateAndWithEmptyList.getAndedPredicates(),
              equalTo(Collections.emptyList()));
    }

    @Test
    void testToSimpleSql() {
        assertThat(this.sqlPredicateAnd.toSimpleSql(), equalTo("('value1' AND 'value2')"));
        System.out.println(this.sqlPredicateAnd.toSimpleSql());
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateAnd.getType(), equalTo(SqlNodeType.PREDICATE_AND));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlLiteralNull> visitor = mock(SqlNodeVisitor.class);
        final SqlLiteralNull sqlLiteralNull = new SqlLiteralNull();
        when(visitor.visit(this.sqlPredicateAnd)).thenReturn(sqlLiteralNull);
        assertThat(this.sqlPredicateAnd.accept(visitor), equalTo(sqlLiteralNull));
    }
}