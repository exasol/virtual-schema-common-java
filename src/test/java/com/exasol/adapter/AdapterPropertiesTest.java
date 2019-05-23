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
    void testIsRefreshingVirtualSchemaRequiredTrue(final String propertyName) {
        this.rawProperties.put(propertyName, "");
        assertThat(AdapterProperties.isRefreshingVirtualSchemaRequired(this.rawProperties), equalTo(true));
    }

    @Test
    void testIsRefreshingVirtualSchemaRequiredFalse() {
        assertThat(AdapterProperties.isRefreshingVirtualSchemaRequired(this.rawProperties), equalTo(false));
    }

    @Test
    void testGetIgnoredErrors() {
        this.rawProperties.put(IGNORE_ERRORS_PROPERTY, "error A, error B,  error C  ");
        assertThat((new AdapterProperties(this.rawProperties)).getIgnoredErrors(),
                containsInAnyOrder("error A", "error B", "error C"));
    }

    @Test
    void testIsLocalSourceFalse() {
        assertThat(AdapterProperties.emptyProperties().isLocalSource(), equalTo(false));
    }

    @Test
    void testIsLocalSourceTrue() {
        this.rawProperties.put(IS_LOCAL_PROPERTY, "true");
        assertThat(new AdapterProperties(this.rawProperties).isLocalSource(), equalTo(true));
    }

    @Test
    void hasUsernameFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasUsername(), equalTo(false));
    }

    @Test
    void hasUsername() {
        this.rawProperties.put(USERNAME_PROPERTY, "USERNAME_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasUsername(), equalTo(true));
    }

    @Test
    void hasPasswordFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasPassword(), equalTo(false));
    }

    @Test
    void hasPassword() {
        this.rawProperties.put(PASSWORD_PROPERTY, "PASSWORD_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasPassword(), equalTo(true));
    }

    @Test
    void hasConnectionStringFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasConnectionString(), equalTo(false));
    }

    @Test
    void hasConnectionString() {
        this.rawProperties.put(CONNECTION_STRING_PROPERTY, "CONNECTION_STRING_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasConnectionString(), equalTo(true));
    }

    @Test
    void hasTableFilterFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasTableFilter(), equalTo(false));
    }

    @Test
    void hasTableFilter() {
        this.rawProperties.put(TABLE_FILTER_PROPERTY, "TABLE_FILTER_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasTableFilter(), equalTo(true));
    }

    @Test
    void hasCatalogNameFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasCatalogName(), equalTo(false));
    }

    @Test
    void hasCatalogName() {
        this.rawProperties.put(CATALOG_NAME_PROPERTY, "CATALOG_NAME_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasCatalogName(), equalTo(true));
    }

    @Test
    void hasSchemaNameFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasSchemaName(), equalTo(false));
    }

    @Test
    void hasSchemaName() {
        this.rawProperties.put(SCHEMA_NAME_PROPERTY, "SCHEMA_NAME_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasSchemaName(), equalTo(true));
    }

    @Test
    void hasConnectionNameFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasConnectionName(), equalTo(false));
    }

    @Test
    void hasConnectionName() {
        this.rawProperties.put(CONNECTION_NAME_PROPERTY, "CONNECTION_NAME_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasConnectionName(), equalTo(true));
    }

    @Test
    void hasDebugAddressFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasDebugAddress(), equalTo(false));
    }

    @Test
    void hasDebugAddress() {
        this.rawProperties.put(DEBUG_ADDRESS_PROPERTY, "DEBUG_ADDRESS_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasDebugAddress(), equalTo(true));
    }

    @Test
    void hasLogLevelFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasLogLevel(), equalTo(false));
    }

    @Test
    void hasLogLevel() {
        this.rawProperties.put(LOG_LEVEL_PROPERTY, "LOG_LEVEL_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasLogLevel(), equalTo(true));
    }

    @Test
    void hasSqlDialectFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasSqlDialect(), equalTo(false));
    }

    @Test
    void hasSqlDialect() {
        this.rawProperties.put(SQL_DIALECT_PROPERTY, "SQL_DIALECT_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasSqlDialect(), equalTo(true));
    }

    @Test
    void hasExcludedCapabilitiesFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasExcludedCapabilities(), equalTo(false));
    }

    @Test
    void hasExcludedCapabilities() {
        this.rawProperties.put(EXCLUDED_CAPABILITIES_PROPERTY, "EXCLUDED_CAPABILITIES_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasExcludedCapabilities(), equalTo(true));
    }

    @Test
    void hasExceptionHandlingFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasExceptionHandling(), equalTo(false));
    }

    @Test
    void hasExceptionHandling() {
        this.rawProperties.put(EXCEPTION_HANDLING_PROPERTY, "EXCEPTION_HANDLING_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasExceptionHandling(), equalTo(true));
    }

    @Test
    void hasIgnoreErrorsFalseByDefault() {
        assertThat(AdapterProperties.emptyProperties().hasIgnoreErrors(), equalTo(false));
    }

    @Test
    void hasIgnoreErrors() {
        this.rawProperties.put(IGNORE_ERRORS_PROPERTY, "IGNORE_ERRORS_PROPERTY");
        assertThat(new AdapterProperties(this.rawProperties).hasIgnoreErrors(), equalTo(true));
    }
}