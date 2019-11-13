package com.exasol.adapter;

import java.util.Set;

/**
 * Factory that creates a Virtual Schema Adapter
 */
public interface AdapterFactory {
    /**
     * List the adapter names the adapter handles
     *
     * @return names for which the adapter is responsible
     */
    public Set<String> getSupportedAdapterNames();

    /**
     * Create a new instance of the Virtual Schema Adapter
     *
     * @return new instance
     */
    public VirtualSchemaAdapter createAdapter();

    /**
     * Get the version of the Virtual Schema Adapter
     *
     * @return Virtual Schema Adapter version
     */
    public String getAdapterVersion();

    /**
     * Get the name of the Virtual Schema Adapter
     *
     * @return Virtual Schema Adapter name
     */
    public String getAdapterName();
}