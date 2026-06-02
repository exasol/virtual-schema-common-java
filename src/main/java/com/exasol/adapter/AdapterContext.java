package com.exasol.adapter;

import com.exasol.telemetry.TelemetryClient;

/**
 * Context information for the {@link VirtualSchemaAdapter}.
 * <p>
 * Note: this currently only contains the {@link TelemetryClient} but may be extended in the future.
 */
public class AdapterContext {
    private final TelemetryClient telemetryClient;

    /**
     * Create a new AdapterContext.
     *
     * @param telemetryClient telemetry client to be used by the adapter for reporting feature usage.
     */
    public AdapterContext(final TelemetryClient telemetryClient) {
        this.telemetryClient = telemetryClient;
    }

    /**
     * Get the telemetry client to be used by the adapter for reporting feature usage.
     * <p>
     * Note:
     * <ul>
     * <li>Class {@link AdapterCallExecutor} already reports the following events: {@code createVirtualSchema}, {@code dropVirtualSchema},
     * {@code refreshVirtualSchema}, {@code setProperties}. The adapter must not report these events again</li>
     * <li>Adapters must not report high frequency features, e.g. for every pushdown.</li>
     * </ul>
     * 
     * @return telemetry client
     */
    public TelemetryClient getTelemetryClient() {
        return this.telemetryClient;
    }
}
