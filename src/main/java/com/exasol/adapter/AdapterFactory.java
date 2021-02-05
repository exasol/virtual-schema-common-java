package com.exasol.adapter;

/**
 * Factory that creates a {@link VirtualSchemaAdapter}
 */
public interface AdapterFactory {
    /**
     * Create a new {@link VirtualSchemaAdapter}
     *
     * @return new instance
     */
    public VirtualSchemaAdapter createAdapter();

    /**
     * Get the version of the {@link VirtualSchemaAdapter}
     *
     * @return Virtual Schema Adapter version
     */
    public String getAdapterVersion();

    /**
     * Get the name of the {@link VirtualSchemaAdapter}
     *
     * @return Virtual Schema Adapter name
     */
    public String getAdapterName();
}