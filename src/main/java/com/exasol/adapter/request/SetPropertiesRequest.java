package com.exasol.adapter.request;

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
     * @param adapterName        name of the adapter that should handle the request
     * @param schemaMetadataInfo schema metadata
     * @param properties         the properties to be set
     */
    public SetPropertiesRequest(final String adapterName, final SchemaMetadataInfo schemaMetadataInfo,
            final Map<String, String> properties) {
        super(adapterName, schemaMetadataInfo, AdapterRequestType.SET_PROPERTIES);
        this.properties = properties;
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