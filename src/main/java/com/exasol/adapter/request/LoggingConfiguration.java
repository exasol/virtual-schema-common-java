package com.exasol.adapter.request;

import java.util.Map;
import java.util.logging.Level;

import static com.exasol.adapter.metadata.SchemaMetadataPropertyConstants.LOG_LEVEL_KEY;
import static com.exasol.adapter.metadata.SchemaMetadataPropertyConstants.REMOTE_DEBUG_CONFIG_KEY;

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
        final Level level = parseLogLevel(properties);
        final boolean logRemotely;
        final String host;
        final int port;
        if (properties.containsKey(REMOTE_DEBUG_CONFIG_KEY)) {
            logRemotely = true;
            final String remoteLoggingAddress = properties.get(REMOTE_DEBUG_CONFIG_KEY);
            if (remoteLoggingAddress.contains(":")) {
                final int separatorIndex = remoteLoggingAddress.indexOf(':');
                host = remoteLoggingAddress.substring(0, separatorIndex);
                port = Integer.parseInt(remoteLoggingAddress.substring(separatorIndex + 1));
            } else {
                host = remoteLoggingAddress;
                port = DEFAULT_REMOTE_LOGGING_PORT;
            }
        } else {
            logRemotely = false;
            host = null;
            port = 0;
        }
        return new LoggingConfiguration(level, logRemotely, host, port);
    }

    private static Level parseLogLevel(final Map<String, String> properties) {
        final Level level;
        if (properties.containsKey(LOG_LEVEL_KEY)) {
            level = Level.parse(properties.get(LOG_LEVEL_KEY));
        } else {
            level = DEFAULT_LOG_LEVEL;
        }
        return level;
    }
}