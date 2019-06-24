package com.exasol.adapter.request;

import static com.exasol.adapter.AdapterProperties.DEBUG_ADDRESS_PROPERTY;
import static com.exasol.adapter.AdapterProperties.LOG_LEVEL_PROPERTY;

import java.util.Map;
import java.util.logging.Level;

/**
 * This class represents the logging configuration set in the request properties
 */
public final class LoggingConfiguration {
    private static final int DEFAULT_REMOTE_LOGGING_PORT = 3000;
    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;
    private final boolean logRemotely;
    private final String host;
    private final int port;
    private final Level level;

    private LoggingConfiguration(final Level level, final boolean logRemotely, final String host, final int port) {
        this.level = level;
        this.logRemotely = logRemotely;
        this.host = host;
        this.port = port;
    }

    /**
     * Check if the adapter should send its log messages to a remote receiver
     *
     * @return <code>true</code> if the adapter should send its log messages to a remote log receiver
     */
    public boolean isRemoteLoggingConfigured() {
        return this.logRemotely;
    }

    /**
     * Get the name of the host where the log should be sent to
     *
     * @return name host name where the log receiver listens
     */
    public String getRemoteLoggingHost() {
        return this.host;
    }

    /**
     * Get the port the remote log receiver is listening on
     *
     * @return remote logging port
     */
    public int getRemoteLoggingPort() {
        return this.port;
    }

    /**
     * Get the log level
     *
     * @return log level
     */
    public Level getLogLevel() {
        return this.level;
    }

    /**
     * Create a new logging configuration from request properties
     *
     * @param properties request properties
     * @return logging configuration
     * @throws IllegalArgumentException if any of the logging configuration parameters can't be parsed
     */
    public static LoggingConfiguration parseFromProperties(final Map<String, String> properties) {
        if (properties.containsKey(DEBUG_ADDRESS_PROPERTY)) {
            return parseFromPropertiesWithDebugAddressGiven(properties);
        } else {
            return createLocalLoggingConfiguration(properties);
        }
    }

    public static LoggingConfiguration parseFromPropertiesWithDebugAddressGiven(final Map<String, String> properties) {
        final String remoteLoggingAddress = properties.get(DEBUG_ADDRESS_PROPERTY);
        if (remoteLoggingAddress.contains(":")) {
            try {
                final int separatorIndex = remoteLoggingAddress.indexOf(':');
                final String host = remoteLoggingAddress.substring(0, separatorIndex);
                final int port = Integer.parseInt(remoteLoggingAddress.substring(separatorIndex + 1));
                return new LoggingConfiguration(parseLogLevel(properties), true, host, port);
            } catch (final NumberFormatException exception) {
                return createLocalLoggingConfiguration(properties);
            }
        } else {
            return new LoggingConfiguration(parseLogLevel(properties), true, remoteLoggingAddress,
                    DEFAULT_REMOTE_LOGGING_PORT);
        }
    }

    private static Level parseLogLevel(final Map<String, String> properties) {
        final Level level;
        if (properties.containsKey(LOG_LEVEL_PROPERTY)) {
            level = Level.parse(properties.get(LOG_LEVEL_PROPERTY));
        } else {
            level = DEFAULT_LOG_LEVEL;
        }
        return level;
    }

    public static LoggingConfiguration createLocalLoggingConfiguration(final Map<String, String> properties) {
        return new LoggingConfiguration(parseLogLevel(properties), false, null, 0);
    }
}