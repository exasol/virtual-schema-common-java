package com.exasol.logging;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class sets up remote logging or falls back to local logging. The later is especially useful in case of unit
 * tests.
 */
public class RemoteLogManager {
    private static final RemoteLogManager instance = new RemoteLogManager();
    private static final Logger ROOT_LOGGER = LogManager.getLogManager().getLogger("");
    private static SocketHandler socketHandler = null;

    /**
     * Get the {@link RemoteLogManager} instance
     *
     * @return manager instance
     */
    public static RemoteLogManager getInstance() {
        return instance;
    }

    /**
     * Configure the logger to write to the console
     *
     * @param logLevel from this level on upward messages are logged
     */
    public void setupConsoleLogger(final Level logLevel) {
        setupRootLogger(new ConsoleHandler(), logLevel);
    }

    private void setupRootLogger(final Handler handler, final Level logLevel) {
        removeExistingHandlers();
        ROOT_LOGGER.setLevel(logLevel);
        addHandler(handler, logLevel);
    }

    private void removeExistingHandlers() {
        for (final Handler handler : ROOT_LOGGER.getHandlers()) {
            ROOT_LOGGER.removeHandler(handler);
        }
    }

    private void addHandler(final Handler handler, final Level logLevel) {
        final Formatter formatter = new CompactFormatter();
        handler.setFormatter(formatter);
        handler.setLevel(logLevel);
        ROOT_LOGGER.addHandler(handler);
    }

    /**
     * Configure the logger to write to a socket
     *
     * @param host     host that the log should be sent to
     * @param port     port on which the log receiver is listening
     * @param logLevel from this level on upward messages are logged
     */
    public void setupRemoteLogger(final String host, final int port, final Level logLevel) {
        try {
            if (socketHandler == null) {
                socketHandler = new SocketHandler(host, port);
            }
            setupRootLogger(socketHandler, logLevel);
            ROOT_LOGGER.info(() -> "Attached to output service with log level " + logLevel + ".");
        } catch (final IOException exception) {
            setupConsoleLogger(logLevel);
            ROOT_LOGGER.warning(() -> "Unable to attch to remote log listener on " + host + ":" + port
                    + ". Falling back to console log.");
        }
    }

    /**
     * Close remote connections if any
     */
    public void close() {
        if (socketHandler != null) {
            socketHandler.close();
        }
    }
}