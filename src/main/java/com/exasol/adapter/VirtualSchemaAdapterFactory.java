package com.exasol.adapter;

/**
 * Factory that creates a {@link VirtualSchemaAdapter}
 */
public interface VirtualSchemaAdapterFactory {
    /**
     * Create a new {@link VirtualSchemaAdapter}
     *
     * @return new instance
     */
    public VirtualSchemaAdapter createVirtualSchemaAdapter();

    /**
     * Get the version of the {@link VirtualSchemaAdapter}
     *
     * @return Virtual Schema Adapter version
     */
    public String getVirtualSchemaAdapterVersion();

    /**
     * Get the name of the {@link VirtualSchemaAdapter}
     *
     * @return Virtual Schema Adapter name
     */
    public String getVirtualSchemaAdapterName();
}