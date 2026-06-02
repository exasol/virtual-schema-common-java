package com.exasol.adapter.request;

import java.util.Map;

/**
 * This class represents the telemetry configuration for the adapter, which is determined based on the request properties.
 */
public class AdapterTelemetryConfiguration {

    /**
     * Name of the adapter property to disable telemetry for the adapter.
     */
    public static final String TELEMETRY_PROPERTY = "TELEMETRY";

    private final boolean telemetryDisabled;

    private AdapterTelemetryConfiguration(final boolean telemetryDisabled) {
        this.telemetryDisabled = telemetryDisabled;
    }

    /**
     * Parse the telemetry configuration from the given properties. If the property is not set, telemetry will be enabled by default.
     * 
     * @param properties the properties to parse the telemetry configuration from
     * @return the parsed telemetry configuration
     */
    public static AdapterTelemetryConfiguration parseFromProperties(final Map<String, String> properties) {
        final String telemetryPropertyValue = properties.get(TELEMETRY_PROPERTY);
        final boolean telemetryDisabled = telemetryPropertyValue != null
                && !Boolean.parseBoolean(telemetryPropertyValue.trim());
        return new AdapterTelemetryConfiguration(telemetryDisabled);
    }

    /**
     * Check if telemetry is enabled for the adapter.
     * 
     * @return {@code true} if telemetry is disabled for the adapter, {@code false} otherwise
     */
    public boolean isTelemetryDisabled() {
        return telemetryDisabled;
    }
}
