package com.exasol.adapter.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.adapter.AdapterProperties;

class AdapterTelemetryConfigurationTest {
    private final Map<String, String> properties = new HashMap<>();

    @Test
    void testTelemetryIsEnabledByDefault() {
        assertThat(createConfiguration().isTelemetryDisabled(), equalTo(false));
    }

    @ParameterizedTest
    @ValueSource(strings = { "true", "TRUE", "TrUe" })
    void testTelemetryIsEnabledWhenPropertyIsTrue(final String value) {
        this.properties.put(AdapterProperties.TELEMETRY_PROPERTY, value);
        assertThat(createConfiguration().isTelemetryDisabled(), equalTo(false));
    }

    @ParameterizedTest
    @ValueSource(strings = { "false", "FALSE", "FaLsE" })
    void testTelemetryIsDisabledWhenPropertyIsFalse(final String value) {
        this.properties.put(AdapterProperties.TELEMETRY_PROPERTY, value);
        assertThat(createConfiguration().isTelemetryDisabled(), equalTo(true));
    }

    @Test
    void testTelemetryIsDisabledWhenPropertyHasNonBooleanValue() {
        this.properties.put(AdapterProperties.TELEMETRY_PROPERTY, "disabled");
        assertThat(createConfiguration().isTelemetryDisabled(), equalTo(true));
    }

    private AdapterTelemetryConfiguration createConfiguration() {
        return AdapterTelemetryConfiguration.parseFromProperties(properties);
    }
}
