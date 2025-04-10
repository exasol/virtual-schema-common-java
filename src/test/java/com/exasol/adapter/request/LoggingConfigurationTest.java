package com.exasol.adapter.request;

import static com.exasol.adapter.properties.AdapterProperties.DEBUG_ADDRESS_PROPERTY;
import static com.exasol.adapter.properties.AdapterProperties.LOG_LEVEL_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

import com.exasol.adapter.SerializationTestUtil;

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
        this.properties.put(DEBUG_ADDRESS_PROPERTY, "");
        assertThat(createLoggingConfiguration(this.properties).isRemoteLoggingConfigured(), equalTo(true));
    }

    @Test
    void testGetRemoteLoggingHostWithDefaultPort() {
        this.properties.put(DEBUG_ADDRESS_PROPERTY, "www.example.com");
        final LoggingConfiguration configuration = createLoggingConfiguration(this.properties);
        assertAll(() -> assertThat(configuration.getRemoteLoggingHost(), equalTo("www.example.com")),
                () -> assertThat(configuration.getRemoteLoggingPort(), equalTo(3000)));
    }

    @Test
    void testGetRemoteLoggingHostWithPort() {
        this.properties.put(DEBUG_ADDRESS_PROPERTY, "www.example.org:4000");
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
        this.properties.put(LOG_LEVEL_PROPERTY, "FINEST");
        assertThat(createLoggingConfiguration(this.properties).getLogLevel(), equalTo(Level.FINEST));
    }

    @Test
    void testFallBackToLocalLoggingInCaseOfIllegalLoggingPort() {
        this.properties.put(DEBUG_ADDRESS_PROPERTY, "www.example.org:illegal_non_numeric_port");
        final LoggingConfiguration configuration = createLoggingConfiguration(this.properties);
        assertThat(configuration.isRemoteLoggingConfigured(), equalTo(false));
    }

    @Test
    void testSerializableDefaultConfig() throws ClassNotFoundException, IOException {
        assertSerializable(createLoggingConfiguration(this.properties));
    }

    @Test
    void testSerializableRemoveConfig() throws ClassNotFoundException, IOException {
        this.properties.put(LOG_LEVEL_PROPERTY, "FINEST");
        this.properties.put(DEBUG_ADDRESS_PROPERTY, "www.example.org:4000");
        assertSerializable(createLoggingConfiguration(this.properties));
    }

    private void assertSerializable(final LoggingConfiguration config)
            throws IOException, ClassNotFoundException, MultipleFailuresError {
        final LoggingConfiguration deserialized = SerializationTestUtil.serializeDeserialize(config,
                LoggingConfiguration.class);
        assertAll(() -> assertThat(deserialized.getLogLevel(), equalTo(config.getLogLevel())),
                () -> assertThat(deserialized.isRemoteLoggingConfigured(), equalTo(config.isRemoteLoggingConfigured())),
                () -> assertThat(deserialized.getRemoteLoggingHost(), equalTo(config.getRemoteLoggingHost())),
                () -> assertThat(deserialized.getRemoteLoggingPort(), equalTo(config.getRemoteLoggingPort())));
    }
}
