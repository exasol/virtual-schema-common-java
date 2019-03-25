package com.exasol.adapter.request;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.SchemaMetadataPropertyConstants;

class LoggingConfigurationTest {
    final Map<String, String> properties = new HashMap<>();

    @Test
    void testIsRemoteLoggingConfigured_No() {
        assertThat(createLoggingConfiguration(this.properties).isRemoteLoggingConfigured(), equalTo(false));
    }

    private LoggingConfiguration createLoggingConfiguration(final Map<String, String> properties) {
        return LoggingConfiguration.parseFromProperties(properties);
    }

    @Test
    void testIsRemoteLoggingConfigured_Yes() {
        this.properties.put(SchemaMetadataPropertyConstants.REMOTE_DEBUG_CONFIG_KEY, "");
        assertThat(createLoggingConfiguration(this.properties).isRemoteLoggingConfigured(), equalTo(true));
    }

    @Test
    void testGetRemoteLoggingHostWithDefaultPort() {
        this.properties.put(SchemaMetadataPropertyConstants.REMOTE_DEBUG_CONFIG_KEY, "www.example.com");
        final LoggingConfiguration configuration = createLoggingConfiguration(this.properties);
        assertAll(() -> assertThat(configuration.getRemoteLoggingHost(), equalTo("www.example.com")),
                () -> assertThat(configuration.getRemoteLoggingPort(), equalTo(3000)));
    }

    @Test
    void testGetRemoteLoggingHostWithPort() {
        this.properties.put(SchemaMetadataPropertyConstants.REMOTE_DEBUG_CONFIG_KEY, "www.example.org:4000");
        final LoggingConfiguration configuration = createLoggingConfiguration(this.properties);
        assertAll(() -> assertThat(configuration.getRemoteLoggingHost(), equalTo("www.example.org")),
                () -> assertThat(configuration.getRemoteLoggingPort(), equalTo(4000)));

    }

    @Test
    void testGetLogLevelDefaultsToInfo() {
        assertThat(createLoggingConfiguration(this.properties).getLogLevel(), equalTo(Level.INFO));
    }

    @Test
    void testGetLogLevel() {
        this.properties.put(SchemaMetadataPropertyConstants.LOG_LEVEL_KEY, "FINEST");
        assertThat(createLoggingConfiguration(this.properties).getLogLevel(), equalTo(Level.FINEST));
    }
}