package com.exasol.adapter;

import static com.exasol.adapter.AdapterProperties.*;
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

    @ValueSource(strings = { CATALOG_NAME_PROPERTY, SCHEMA_NAME_PROPERTY, CONNECTION_NAME_PROPERTY,
            CONNECTION_STRING_PROPERTY, USERNAME_PROPERTY, PASSWORD_PROPERTY, DEBUG_ADDRESS_PROPERTY,
            LOG_LEVEL_PROPERTY, SQL_DIALECT_PROPERTY, EXCLUDED_CAPABILITIES_PROPERTY, EXCEPTION_HANDLING_PROPERTY })
    @ParameterizedTest
    void testGetCatalogName(final String property) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final String expectedValue = property + "_VALUE";
        this.rawProperties.put(property, expectedValue);
        final AdapterProperties properties = createProperties();
        final String methodName = "get" + StringUtils.toCamelCase(property);
        final String value = (String) AdapterProperties.class.getMethod(methodName).invoke(properties);
        assertThat(value, equalTo(expectedValue));
    }

    private AdapterProperties createProperties() {
        return new AdapterProperties(this.rawProperties);
    }

    @Test
    void testGetFilteredTables() {
        this.rawProperties.put(AdapterProperties.TABLE_FILTER_PROPERTY, "Table a,Table B, TABLE  C    ,  table d  ");
        final AdapterProperties properties = createProperties();
        assertThat(properties.getFilteredTables(), containsInAnyOrder("Table a", "Table B", "TABLE  C", "table d"));
    }

    @ValueSource(strings = { CONNECTION_STRING_PROPERTY, CONNECTION_NAME_PROPERTY, USERNAME_PROPERTY, PASSWORD_PROPERTY,
            SCHEMA_NAME_PROPERTY, CATALOG_NAME_PROPERTY, TABLE_FILTER_PROPERTY })
    @ParameterizedTest
    void testtIsRefreshingVirtualSchemaRequiredTrue(final String propertyName) {
        this.rawProperties.put(propertyName, "");
        assertThat(AdapterProperties.isRefreshingVirtualSchemaRequired(this.rawProperties), equalTo(true));
    }

    @Test
    void testtIsRefreshingVirtualSchemaRequiredFalse() {
        assertThat(AdapterProperties.isRefreshingVirtualSchemaRequired(this.rawProperties), equalTo(false));
    }

    @Test
    void testGetIgnoredErrors() {
        this.rawProperties.put(IGNORE_ERRORS_PROPERTY, "error A, error B,  error C  ");
        assertThat((new AdapterProperties(this.rawProperties)).getIgnoredErrors(),
                containsInAnyOrder("error A", "error B", "error C"));
    }
}