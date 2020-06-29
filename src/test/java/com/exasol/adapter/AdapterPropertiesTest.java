package com.exasol.adapter;

import static com.exasol.adapter.AdapterProperties.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.exasol.utils.StringUtils;

class AdapterPropertiesTest {
    private static final String PROPERTY_SUFFIX = "_PROPERTY";
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
            DEBUG_ADDRESS_PROPERTY, LOG_LEVEL_PROPERTY, SQL_DIALECT_PROPERTY, EXCLUDED_CAPABILITIES_PROPERTY,
            EXCEPTION_HANDLING_PROPERTY })
    @ParameterizedTest
    void testGetStringProperty(final String property) throws IllegalAccessException, IllegalArgumentException,
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

    @ValueSource(strings = { CONNECTION_NAME_PROPERTY, SCHEMA_NAME_PROPERTY, CATALOG_NAME_PROPERTY,
            TABLE_FILTER_PROPERTY, BINARY_COLUMN_HANDLING_PROPERTY })
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
    void testGetIgnoredErrorsReturnsEmptyListByDefault() {
        assertThat(AdapterProperties.emptyProperties().getIgnoredErrors(), emptyIterableOf(String.class));
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

    @CsvSource({ "IGNORE, IGNORE", "ENCODE_BASE64, ENCODE_BASE64" })
    @ParameterizedTest
    void testGetBinaryColumnHandling(final String value, final BinaryColumnHandling binaryColumnHandling) {
        this.rawProperties.put(BINARY_COLUMN_HANDLING_PROPERTY, value);
        assertThat(new AdapterProperties(this.rawProperties).getBinaryColumnHandling(), equalTo(binaryColumnHandling));
    }

    @Test
    void testGetBinaryColumnHandlingIsIgnoreByDefault() {
        assertThat(AdapterProperties.emptyProperties().getBinaryColumnHandling(), equalTo(BinaryColumnHandling.IGNORE));
    }

    @Test
    void testGetUnknownBinaryColumnHandlingThrowsException() {
        this.rawProperties.put(BINARY_COLUMN_HANDLING_PROPERTY, "THIS_VALUE_DOES_NOT_EXIST");
        assertThrows(IllegalArgumentException.class,
                () -> new AdapterProperties(this.rawProperties).getBinaryColumnHandling());
    }

    @MethodSource("getAdapterPropertyNames")
    @ParameterizedTest
    void testHasNamedPropertyFalseByDefault(final String propertyName) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final AdapterProperties emptyProperties = AdapterProperties.emptyProperties();
        final Boolean hasNamedProperty = invokePropertyExistenceCheckMethod(propertyName, emptyProperties);
        assertThat(hasNamedProperty, equalTo(false));
    }

    static public Stream<String> getAdapterPropertyNames() {
        return Arrays.stream(AdapterProperties.class.getDeclaredFields()) //
                .map(Field::getName) //
                .filter(name -> name.endsWith(PROPERTY_SUFFIX)) //
                .map(name -> name.replace(PROPERTY_SUFFIX, ""));
    }

    public boolean invokePropertyExistenceCheckMethod(final String propertyName, final AdapterProperties properties)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String methodName = "has" + StringUtils.toCamelCase(propertyName);
        final Method method = properties.getClass().getDeclaredMethod(methodName);
        return (Boolean) method.invoke(properties);
    }

    @MethodSource("getAdapterPropertyNames")
    @ParameterizedTest
    void testHasNamedProperty(final String propertyName) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final AdapterProperties properties = createPropertyListWithSpecificPropertySetToAValue(propertyName);
        final Boolean hasNamedProperty = invokePropertyExistenceCheckMethod(propertyName, properties);
        assertThat(hasNamedProperty, equalTo(true));
    }

    public AdapterProperties createPropertyListWithSpecificPropertySetToAValue(final String propertyName) {
        this.rawProperties.put(propertyName, propertyName + "_VALUE");
        return new AdapterProperties(this.rawProperties);
    }
}