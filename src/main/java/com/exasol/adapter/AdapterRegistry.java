package com.exasol.adapter;

import java.util.*;
import java.util.logging.Logger;

/**
 * {@link VirtualSchemaAdapter}s need to be registered in the {@link AdapterRegistry} in order to receive requests from
 * the dispatcher.
 */
public final class AdapterRegistry {
    private static final Logger LOGGER = Logger.getLogger(AdapterRegistry.class.getName());
    private static AdapterRegistry instance = new AdapterRegistry();
    private final Map<String, VirtualSchemaAdapter> registeredAdapters = new HashMap<>();

    /**
     * Get the singleton instance of the {@link AdapterRegistry}
     *
     * @return singleton instance
     */
    public static AdapterRegistry getInstance() {
        return instance;
    }

    /**
     * Get a list of all currently registered Virtual Schema Adapters
     *
     * @return list of adapters
     */
    public List<VirtualSchemaAdapter> getRegisteredAdapters() {
        return new ArrayList<>(this.registeredAdapters.values());
    }

    /**
     * Register a new adapter
     *
     * @param name    name under which the adapter is registered
     * @param adapter adapter instance
     */
    public void registerAdapter(final String name, final VirtualSchemaAdapter adapter) {
        LOGGER.fine(() -> "Registering adapter \"" + name + "\" (" + adapter.getClass().getName() + ")");
        this.registeredAdapters.put(name, adapter);
    }

    /**
     * Get the Virtual Schema Adapter registered under the given name
     *
     * @param name name of the adapter
     * @return adapter instance
     */
    public VirtualSchemaAdapter getAdapterForName(final String name) {
        if (hasAdapterWithName(name)) {
            return this.registeredAdapters.get(name);
        } else {
            throw new IllegalArgumentException(
                    "Unknown Virtual Schema Adapter \"" + name + "\" requested. " + describe());
        }
    }

    /**
     * Check if an adapter with the given name is registered
     *
     * @param name adapter name to be searched for
     * @return <code>true</code> if an adapter is registered under that name
     */
    public boolean hasAdapterWithName(final String name) {
        return this.registeredAdapters.containsKey(name);
    }

    /**
     * Remove all registered adapters from the registry
     */
    public void clear() {
        this.registeredAdapters.clear();
    }

    /**
     * Describe the contents of the registry
     *
     * @return description
     */
    public String describe() {
        final StringBuilder builder = new StringBuilder("Currently registered Virtual Schema Adapters: ");
        boolean first = true;
        for (final String name : this.registeredAdapters.keySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("\"");
            builder.append(name);
            builder.append("\"");
        }
        return builder.toString();
    }
}