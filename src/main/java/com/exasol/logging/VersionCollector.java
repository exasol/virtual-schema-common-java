package com.exasol.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.exasol.errorreporting.ExaError;

/**
 * This class fetches the version of the jar from the metadata in the jar file.
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
     * 
     * @deprecated use {@link #VersionCollector(String)} instead to specify the path to the version file. The default path is
     *             {@code "META-INF/maven/com.exasol/virtual-schema-common-java/pom.properties"}.
     */
    @Deprecated(since = "18.0.2", forRemoval = true)
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
        final ClassLoader loader = getClassLoader();
        final InputStream stream = loader.getResourceAsStream(this.path);
        if (stream == null) {
            return "UNKNOWN";
        }
        try (final InputStream resourceStream = stream) {
            properties.load(resourceStream);
        } catch (final IOException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSCOMJAVA-31")
                    .message("Unable to read the version from the file: {{path}}.") //
                    .parameter("path", this.path).toString(), exception);
        }
        return properties.getProperty(VERSION, "UNKNOWN");
    }

    private ClassLoader getClassLoader() {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }
        return VersionCollector.class.getClassLoader();
    }
}
