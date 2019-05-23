package com.exasol.adapter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The main job of this class is to turn user-defined key-value-pairs into
 * concrete properties that the adapter knows. This improves the code
 * readability and robustness.
 * <p>
 * It is still possible to access the raw keys and values in order to support
 * properties that are specific to an adapter type an can therefore not be
 * covered in the common module.
 */
public class AdapterProperties extends AbstractAdapterProperties {
    public static final String TABLE_FILTER_PROPERTY = "TABLE_FILTER";
    public static final String CATALOG_NAME_PROPERTY = "CATALOG_NAME";
    public static final String SCHEMA_NAME_PROPERTY = "SCHEMA_NAME";
    public static final String CONNECTION_NAME_PROPERTY = "CONNECTION_NAME";
    public static final String CONNECTION_STRING_PROPERTY = "CONNECTION_STRING";
    public static final String DEBUG_ADDRESS_PROPERTY = "DEBUG_ADDRESS";
    public static final String LOG_LEVEL_PROPERTY = "LOG_LEVEL";
    public static final String SQL_DIALECT_PROPERTY = "SQL_DIALECT";
    public static final String EXCLUDED_CAPABILITIES_PROPERTY = "EXCLUDED_CAPABILITIES";
    public static final String EXCEPTION_HANDLING_PROPERTY = "EXCEPTION_HANDLING";
    public static final String IGNORE_ERRORS_PROPERTY = "IGNORE_ERRORS";
    public static final String IS_LOCAL_PROPERTY = "IS_LOCAL";
    public static final String USERNAME_PROPERTY = "USERNAME";
    @SuppressWarnings("squid:S2068") // Sonar mistakes this for a hard-coded credential
    public static final String PASSWORD_PROPERTY = "PASSWORD";

    /**
     * Create a new instance of {@link AdapterProperties}
     *
     * @param properties adapter properties
     */
    public AdapterProperties(final Map<String, String> properties) {
        super(properties);
    }

    /**
     * Get the catalog name
     *
     * @return catalog name
     */
    public String getCatalogName() {
        return get(CATALOG_NAME_PROPERTY);
    }

    /**
     * Get the schema name
     *
     * @return schema name
     */
    public String getSchemaName() {
        return get(SCHEMA_NAME_PROPERTY);
    }

    /**
     * Get the connection name
     *
     * @return connection name
     */
    public String getConnectionName() {
        return get(CONNECTION_NAME_PROPERTY);
    }

    /**
     * Get the connection string
     *
     * @return connection string
     */
    public String getConnectionString() {
        return get(CONNECTION_STRING_PROPERTY);
    }

    /**
     * Get the user name
     *
     * @return user name
     */
    public String getUsername() {
        return get(USERNAME_PROPERTY);
    }

    /**
     * Get the password
     *
     * @return password
     */
    public String getPassword() {
        return get(PASSWORD_PROPERTY);
    }

    /**
     * Get the debug address
     *
     * @return debug address
     */
    public String getDebugAddress() {
        return get(DEBUG_ADDRESS_PROPERTY);
    }

    /**
     * Get the log level
     *
     * @return log level
     */
    public String getLogLevel() {
        return get(LOG_LEVEL_PROPERTY);
    }

    /**
     * Get the sql dialect
     *
     * @return sql dialect
     */
    public String getSqlDialect() {
        return get(SQL_DIALECT_PROPERTY);
    }

    /**
     * Get the excluded capabilities
     *
     * @return excluded capabilities
     */
    public String getExcludedCapabilities() {
        return get(EXCLUDED_CAPABILITIES_PROPERTY);
    }

    /**
     * Get the exception handling
     *
     * @return exception handling
     */
    public String getExceptionHandling() {
        return get(EXCEPTION_HANDLING_PROPERTY);
    }

    /**
     * Get the list of ignored errors
     *
     * @return list of ignored errors
     */
    public List<String> getIgnoredErrors() {
        return splitCommaSepartedListWithEmptyAsDefault(IGNORE_ERRORS_PROPERTY);
    }

    private List<String> splitCommaSepartedListWithEmptyAsDefault(final String property) {
        return containsKey(property) ? splitCommaSeparatedList(get(property)) : Collections.emptyList();
    }

    private List<String> splitCommaSeparatedList(final String value) {
        return Arrays.stream(value.split(",")) //
                .map(String::trim) //
                .collect(Collectors.toList());
    }

    /**
     * Get the list of tables for which the metadata will be read from the remote
     * source
     *
     * @return list of tables serving as positive filter criteria
     */
    public List<String> getFilteredTables() {
        return splitCommaSepartedListWithEmptyAsDefault(TABLE_FILTER_PROPERTY);
    }

    /**
     * Check whether any of the given properties causes a refresh of the virtual
     * schema
     *
     * @param changedProperties map of properties that were changed
     * @return <code>true</code> if any of the changes makes refreshing the virtual
     *         schema necessary
     */
    public static boolean isRefreshingVirtualSchemaRequired(final Map<String, String> changedProperties) {
        return changedProperties.containsKey(CONNECTION_STRING_PROPERTY) //
                || changedProperties.containsKey(CONNECTION_NAME_PROPERTY) //
                || changedProperties.containsKey(USERNAME_PROPERTY) //
                || changedProperties.containsKey(PASSWORD_PROPERTY) //
                || changedProperties.containsKey(SCHEMA_NAME_PROPERTY) //
                || changedProperties.containsKey(CATALOG_NAME_PROPERTY) //
                || changedProperties.containsKey(TABLE_FILTER_PROPERTY);
    }

    /**
     * Check if the data source is the local Exasol instance
     *
     * @return <code>true</code> if the data source and the database containing the
     *         Virtual Schema are hosted on the same Exasol cluster
     */
    public boolean isLocalSource() {
        return isEnabled(IS_LOCAL_PROPERTY);
    }

    /**
     * Check if the username property is set
     *
     * @return <code>true</code> if username property is set
     */
    public boolean hasUsername() {
        return containsKey(USERNAME_PROPERTY);
    }

    /**
     * Check if the password property is set
     *
     * @return <code>true</code> if password property is set
     */
    public boolean hasPassword() {
        return containsKey(PASSWORD_PROPERTY);
    }

    /**
     * Check if the connection string property is set
     *
     * @return <code>true</code> if connection string property is set
     */
    public boolean hasConnectionString() {
        return containsKey(CONNECTION_STRING_PROPERTY);
    }

    /**
     * Check if the table filter property is set
     *
     * @return <code>true</code> if table filter property is set
     */
    public boolean hasTableFilter() {
        return containsKey(TABLE_FILTER_PROPERTY);
    }

    /**
     * Check if the catalog name property is set
     *
     * @return <code>true</code> if catalog name property is set
     */
    public boolean hasCatalogName() {
        return containsKey(CATALOG_NAME_PROPERTY);
    }

    /**
     * Check if the schema name property is set
     *
     * @return <code>true</code> if schema name property is set
     */
    public boolean hasSchemaName() {
        return containsKey(SCHEMA_NAME_PROPERTY);
    }

    /**
     * Check if the connection name property is set
     *
     * @return <code>true</code> if connection name property is set
     */
    public boolean hasConnectionName() {
        return containsKey(CONNECTION_NAME_PROPERTY);
    }

    /**
     * Check if the debug address property is set
     *
     * @return <code>true</code> if debug address property is set
     */
    public boolean hasDebugAddress() {
        return containsKey(DEBUG_ADDRESS_PROPERTY);
    }

    /**
     * Check if the log level property is set
     *
     * @return <code>true</code> if log level property is set
     */
    public boolean hasLogLevel() {
        return containsKey(LOG_LEVEL_PROPERTY);
    }

    /**
     * Check if the SQL dialect property is set
     *
     * @return <code>true</code> if SQL dialect property is set
     */
    public boolean hasSqlDialect() {
        return containsKey(SQL_DIALECT_PROPERTY);
    }

    /**
     * Check if the excluded capabilities property is set
     *
     * @return <code>true</code> if excluded capabilities property is set
     */
    public boolean hasExcludedCapabilities() {
        return containsKey(EXCLUDED_CAPABILITIES_PROPERTY);
    }

    /**
     * Check if the exception handling property is set
     *
     * @return <code>true</code> if exception handling property is set
     */
    public boolean hasExceptionHandling() {
        return containsKey(EXCEPTION_HANDLING_PROPERTY);
    }

    /**
     * Check if the ignore errors property is set
     *
     * @return <code>true</code> if ignore errors property is set
     */
    public boolean hasIgnoreErrors() {
        return containsKey(IGNORE_ERRORS_PROPERTY);
    }

    /**
     * Get empty property list
     *
     * @return empty properties
     */
    public static AdapterProperties emptyProperties() {
        return new AdapterProperties(Collections.emptyMap());
    }
}