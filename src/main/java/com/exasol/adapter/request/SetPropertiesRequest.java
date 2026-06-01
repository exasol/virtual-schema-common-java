package com.exasol.adapter.request;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterCallExecutor;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.SchemaMetadataInfo;

/**
 * This class represents a request that tells a Virtual Schema Adapter to set the provided properties
 */
public class SetPropertiesRequest extends AbstractAdapterRequest {
    private final Map<String, String> properties;

    /**
     * Create a new request of type {@link SetPropertiesRequest}
     *
     * @param schemaMetadataInfo schema metadata
     * @param properties         the properties to be set
     */
    public SetPropertiesRequest(final SchemaMetadataInfo schemaMetadataInfo, final Map<String, String> properties) {
        super(schemaMetadataInfo, AdapterRequestType.SET_PROPERTIES);
        this.properties = properties == null ? emptyMap() : unmodifiableMap(new HashMap<>(properties));
    }

    /**
     * Get the properties to be set
     *
     * @return properties to be set
     */
    public Map<String, String> getProperties() {
        return this.properties;
    }

    @Override
    public String executeWith(final AdapterCallExecutor adapterCallExecutor, final ExaMetadata metadata)
            throws AdapterException {
        return adapterCallExecutor.executeSetPropertiesRequest(this, metadata);
    }
}
