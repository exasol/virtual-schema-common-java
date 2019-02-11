package com.exasol.adapter.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlPredicateAndTest {
    private static final String VALUE = "2019-02-07";
    private SqlPredicateAnd sqlPredicateAnd;
    private SqlPredicateAnd sqlPredicateAndWithEmptyList;
    private List<SqlNode> andedPredicates;

    @BeforeEach
    void setUp() {
        this.andedPredicates = new ArrayList<>();
        this.andedPredicates.add(new SqlLiteralNull());
        this.andedPredicates.add(new SqlLiteralBool(true));
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
        assertThat(this.sqlPredicateAnd.toSimpleSql(), equalTo("(NULL AND true)"));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateAnd.getType(), equalTo(SqlNodeType.PREDICATE_AND));
    }
}