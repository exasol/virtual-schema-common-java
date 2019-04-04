package com.exasol.adapter;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.utils.StringUtils;

class AdapterPropertiesTest {
    private Map<String, String> rawProperties;

    @BeforeEach
    void beforeEach() {
        this.rawProperties = new HashMap<>();
    }

    @Test
    void testEmptyProperties() {
        assertThat(AdapterProperties.emptyProperties(), equalTo(new AdapterProperties(Collections.emptyMap())));
    }

    @ValueSource(strings = { "CATALOG_NAME", "SCHEMA_NAME", "CONNECTION_NAME", "CONNECTION_STRING", "USERNAME",
          "PASSWORD", "DEBUG_ADDRESS", "LOG_LEVEL"})
    @ParameterizedTest
    void testGetCatalogName(final String property) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final String expectedValue = property + "_VALUE";
        this.rawProperties.put(property, expectedValue);
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        final String methodName = "get" + StringUtils.toCamelCase(property);
        final String value = (String) AdapterProperties.class.getMethod(methodName).invoke(properties);
        assertThat(value, equalTo(expectedValue));
    }

    @Test
    void testGetFilteredTables() {
        this.rawProperties.put(AdapterProperties.TABLE_FILTER_PROPERTY, "Table a,Table B, TABLE  C    ,  table d  ");
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        assertThat(properties.getFilteredTables(), containsInAnyOrder("Table a", "Table B", "TABLE  C", "table d"));
    }
}