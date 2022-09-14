package com.exasol.logging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hamcrest.Matcher;
import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemErrGuard;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SystemErrGuard.class)
class RemoteLogManagerTest {

    private static final String CLASS_TAG = "\\[c\\.e\\.l\\.RemoteLogManagerTest\\]";
    static final String TIMESTAMP_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}";
    private static final Logger LOGGER = Logger.getLogger(RemoteLogManagerTest.class.getName());
    static final String LINEFEED_PATTERN = linefeedPattern();

    static Matcher<String> matchesTimeStamp(final String content) {
        return matchesPattern(TIMESTAMP_PATTERN + content + LINEFEED_PATTERN);
    }

    private RemoteLogManager logManager;

    static String linefeedPattern() {
        switch (System.lineSeparator()) {
        case "\r\n":
            return "\\r\\n";
        case "\r":
            return "\\r";
        case "\n": // falling through intentionally
        default:
            return "\\n";
        }
    }

    @BeforeEach
    void BeforeEach() {
        this.logManager = new RemoteLogManager();
    }

    @AfterEach
    void afterEach() {
        this.logManager.close();
    }

    @Test
    void testSetupConsoleLogging(final Capturable stream) throws IOException {
        this.logManager.setupConsoleLogger(Level.INFO);
        stream.capture();
        LOGGER.info("Hello.");
        assertThat(stream.getCapturedData(), matchesTimeStamp(" INFO +" + CLASS_TAG + " Hello."));
    }

    @Test
    void testSetupConsoleLoggingWithMoreDetailedLogLevel(final Capturable stream) throws IOException {
        this.logManager.setupConsoleLogger(Level.ALL);
        stream.capture();
        LOGGER.finest(() -> "Hello.");
        assertThat(stream.getCapturedData(), matchesTimeStamp(" FINEST +" + CLASS_TAG + " Hello."));
    }

    @Test
    void testRemoteSocketLogging() throws UnknownHostException, IOException {
        final InetAddress loop = InetAddress.getLoopbackAddress();
        String received = null;
        try (final ServerSocket server = new ServerSocket(0, 1, loop)) {
            server.setSoTimeout(2000);
            final String loopBackAddress = loop.getHostName();
            final int port = server.getLocalPort();
            attachToLogServiceInParallelThread(loopBackAddress, port);
            try (final Socket client = server.accept()) {
                client.setSoTimeout(2000);
                received = readLogMessageFromSocket(client);
            }
        }
        assertThat(received,
                matchesPattern(TIMESTAMP_PATTERN + " INFO +\\[.*?\\] Attached to output service with log level ALL."));
    }

    private Thread attachToLogServiceInParallelThread(final String loopBackAddress, final int port) {
        final Thread thread = new Thread(() -> {
            waitForAcceptingSeverSocketToGetReady();
            this.logManager.setupRemoteLogger(loopBackAddress, port, Level.ALL);
        });
        thread.start();
        return thread;
    }

    /**
     * While sleeping in tests is generally not a good idea since it makes the test prone to race conditions, here we do
     * not have much choice. Thread notification from the accepting thread to the adapter thread does not work because
     * the <code>accept()</code> method blocks the accepting thread. So we can't send a notification from that thread to
     * let the adapter thread know that the server socket is ready.
     */
    @SuppressWarnings("squid:S2925")
    private void waitForAcceptingSeverSocketToGetReady() {
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String readLogMessageFromSocket(final Socket socket) throws IOException {
        String received = null;
        try (final InputStreamReader inputReader = new InputStreamReader(socket.getInputStream())) {
            try (final BufferedReader reader = new BufferedReader(inputReader)) {
                received = reader.readLine();
            }
        }
        return received;
    }

    @Test
    void testFallBackFromRemoteLoggingToConsoleLogging(final Capturable stream) {
        stream.capture();
        this.logManager.setupRemoteLogger("this.hostname.should.not.exist.exasol.com", 3000, Level.ALL);
        assertThat(stream.getCapturedData(), matchesPattern(".*Falling back to console log." + System.lineSeparator()));
    }
}