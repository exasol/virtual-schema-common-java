package com.exasol.adapter;

import java.util.*;
import java.util.logging.Logger;

/**
 * {@link VirtualSchemaAdapter}s need to be registered in the {@link AdapterRegistry} in order to receive requests from
 * the dispatcher.
 */
public final class AdapterRegistry {
    private static final Logger LOGGER = Logger.getLogger(AdapterRegistry.class.getName());
    private static AdapterRegistry instance;
    private final Map<String, AdapterFactory> registeredFactories = new HashMap<>();

    /**
     * Get the singleton instance of the {@link AdapterRegistry}
     *
     * @return singleton instance
     */
    public static final synchronized AdapterRegistry getInstance() {
        if (instance == null) {
            LOGGER.finer(() -> "Instanciating Virtual Schema Adapter registry and loading adapter factories.");
            instance = new AdapterRegistry();
            instance.loadAdapterFactories();
        }
        return instance;
    }

    private void loadAdapterFactories() {
        final ServiceLoader<AdapterFactory> serviceLoader = ServiceLoader.load(AdapterFactory.class);
        final Iterator<AdapterFactory> factories = serviceLoader.iterator();
        while (factories.hasNext()) {
            final AdapterFactory factory = factories.next();
            final Set<String> supportedAdapterNames = factory.getSupportedAdapterNames();
            LOGGER.fine(() -> "Registering factory for Virtual Schema Adapter \"" + factories.getClass().getName()
                    + "\" which supports: " + String.join(", ", supportedAdapterNames));
            for (final String adapterName : supportedAdapterNames) {
                registerAdapterFactory(adapterName, factory);
            }
        }
    }

    /**
     * Register a factory for a {@link VirtualSchemaAdapter}
     *
     * @param factory     factory that can create the adapter
     * @param adapterName name of the adapter
     */
    public void registerAdapterFactory(final String adapterName, final AdapterFactory factory) {
        this.registeredFactories.put(adapterName, factory);
    }

    /**
     * Get a list of all currently registered Virtual Schema Adapters
     *
     * @return list of adapter factories
     */
    public List<AdapterFactory> getRegisteredAdapterFactories() {
        return new ArrayList<>(this.registeredFactories.values());
    }

    /**
     * Get the Virtual Schema Adapter registered under the given name
     *
     * @param name name of the adapter
     * @return adapter instance
     */
    public VirtualSchemaAdapter getAdapterForName(final String name) {
        if (hasAdapterWithName(name)) {
            final AdapterFactory factory = this.registeredFactories.get(name);
            LOGGER.config(() -> "Loading Virtual Schema Adapter: " + factory.getAdapterName() + " "
                    + factory.getAdapterVersion());
            return factory.createAdapter();
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
        return this.registeredFactories.containsKey(name);
    }

    /**
     * Remove all registered adapters from the registry
     */
    public void clear() {
        this.registeredFactories.clear();
    }

    /**
     * Describe the contents of the registry
     *
     * @return description
     */
    public String describe() {
        if (this.registeredFactories.isEmpty()) {
            return "No Virtual Schema Adapter factories are currently reqistered.";
        } else {
            final StringBuilder builder = new StringBuilder("Currently registered Virtual Schema Adapter factories: ");
            boolean first = true;
            for (final String name : this.registeredFactories.keySet()) {
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
}