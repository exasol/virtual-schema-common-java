package com.exasol.logging;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class sets up remote logging or falls back to local logging. The later is especially useful in case of unit
 * tests.
 */
public class RemoteLogManager {
    private static final Logger ROOT_LOGGER = LogManager.getLogManager().getLogger("");
    private static final Logger LOGGER = Logger.getLogger(RemoteLogManager.class.getName());
    private SocketHandler socketHandler = null;

    /**
     * Configure the logger to write to the console
     *
     * @param logLevel from this level on upward messages are logged
     */
    public void setupConsoleLogger(final Level logLevel) {
        setupRootLoggerForLocalLogging(logLevel);
        LOGGER.info(() -> "Set up local logging with log level " + logLevel + ".");
    }

    private void setupRootLoggerForLocalLogging(final Level logLevel) {
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
            if (this.socketHandler == null) {
                this.socketHandler = new SocketHandler(host, port);
            }
            setupRootLogger(this.socketHandler, logLevel);
            LOGGER.info(() -> "Attached to output service with log level " + logLevel + ".");
        } catch (final IOException exception) {
            setupRootLoggerForLocalLogging(logLevel);
            LOGGER.warning(() -> "Unable to attach to remote log listener on " + host + ":" + port
                    + ". Falling back to console log.");
        }
    }

    /**
     * Close remote connections if any
     */
    public void close() {
        if (this.socketHandler != null) {
            this.socketHandler.close();
        }
    }
}