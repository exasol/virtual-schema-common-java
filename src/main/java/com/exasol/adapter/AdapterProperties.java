package com.exasol.adapter;

import java.util.*;
import java.util.stream.Collectors;

public class AdapterProperties extends AbstractAdapterProperties {
    static final String TABLE_FILTER_PROPERTY = "TABLE_FILTER";
    private static final String CATALOG_NAME_PROPERTY = "CATALOG_NAME";

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
     * Get the list of tables for which the metadata will be read from the remote source
     *
     * @return list of tables serving as positive filter criteria
     */
    public List<String> getFilteredTables() {
        if (containsKey(TABLE_FILTER_PROPERTY)) {
            return Arrays.stream(get(TABLE_FILTER_PROPERTY).split(",")) //
                    .map(String::trim) //
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Get empty map
     *
     * @return empty map
     */
    public static AbstractAdapterProperties emptyProperties() {
        return new AdapterProperties(Collections.emptyMap());
    }
}