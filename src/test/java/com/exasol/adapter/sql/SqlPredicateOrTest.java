package com.exasol.adapter.sql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.mocking.MockUtils;

class SqlPredicateOrTest {
    private SqlPredicateOr sqlPredicateOr;
    private SqlPredicateOr sqlPredicateOrWithEmptyList;
    private List<SqlNode> orPredicates;

    @BeforeEach
    void setUp() {
        this.orPredicates = new ArrayList<>();
        this.orPredicates.add(new SqlLiteralString("value1"));
        this.orPredicates.add(new SqlLiteralString("value2"));
        this.sqlPredicateOr = new SqlPredicateOr(this.orPredicates);
        this.sqlPredicateOrWithEmptyList = new SqlPredicateOr(null);
    }

    @Test
    void testGetOrPredicates() {
        assertThat(this.sqlPredicateOr.getOrPredicates(), equalTo(this.orPredicates));
    }

    @Test
    void testGetAndedPredicatesEmptyList() {
        assertThat(this.sqlPredicateOrWithEmptyList.getOrPredicates(), equalTo(Collections.emptyList()));
    }

    @Test
    void testGetType() {
        assertThat(this.sqlPredicateOr.getType(), equalTo(SqlNodeType.PREDICATE_OR));
    }

    @Test
    void testAccept() throws AdapterException {
        final SqlNodeVisitor<SqlPredicateOr> visitor = MockUtils.mockSqlNodeVisitor();
        when(visitor.visit(this.sqlPredicateOr)).thenReturn(this.sqlPredicateOr);
        assertThat(this.sqlPredicateOr.accept(visitor), equalTo(this.sqlPredicateOr));
    }
}