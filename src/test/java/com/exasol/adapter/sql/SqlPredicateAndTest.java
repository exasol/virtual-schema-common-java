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
        andedPredicates = new ArrayList<>();
        andedPredicates.add(new SqlLiteralNull());
        andedPredicates.add(new SqlLiteralBool(true));
        sqlPredicateAnd = new SqlPredicateAnd(andedPredicates);
        sqlPredicateAndWithEmptyList = new SqlPredicateAnd(Collections.emptyList());
    }

    @Test
    void testGetAndedPredicates() {
        assertThat(sqlPredicateAndWithEmptyList.getAndedPredicates(), equalTo(Collections.emptyList()));
        assertThat(sqlPredicateAnd.getAndedPredicates(), equalTo(andedPredicates));
    }

    @Test
    void testToSimpleSql() {
        assertThat(sqlPredicateAnd.toSimpleSql(), equalTo("(NULL AND true)"));
    }

    @Test
    void testGetType() {
        assertThat(sqlPredicateAnd.getType(), equalTo(SqlNodeType.PREDICATE_AND));
    }
}