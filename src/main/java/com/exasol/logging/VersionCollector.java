package com.exasol.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.exasol.errorreporting.ExaError;

/**
 * This class fetches the version of the jar from the metadate in the jar file.
 */
public class VersionCollector {
    private static final String DEFAULT_PATH = "META-INF/maven/com.exasol/virtual-schema-common-java/pom.properties";
    private static final String VERSION = "version";
    private final String path;

    /**
     * Instantiates a new Version collector.
     *
     * @param path the path
     */
    public VersionCollector(final String path) {
        this.path = path;
    }

    /**
     * Instantiates a new Version collector.
     */
    public VersionCollector() {
        this.path = DEFAULT_PATH;
    }

    /**
     * Gets version number of the current JAR file.
     *
     * @return the version number
     */
    public String getVersionNumber() {
        final Properties properties = new Properties();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = loader.getResourceAsStream(this.path);
        if (stream == null) {
            return "UNKNOWN";
        }
        try {
            properties.load(stream);
        } catch (final IOException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-31")
                    .message("Unable to read the version from the file: {{path}}.") //
                    .parameter("path", this.path).toString(), exception);
        }
        return properties.getProperty(VERSION);
    }
}
