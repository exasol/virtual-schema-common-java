package com.exasol.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionCollector {
    private static final String PATH = "META-INF/maven/com.exasol/virtual-schema-common-java/pom.properties";
    private static final String VERSION = "version";
    private final String path;

    public VersionCollector(final String path) {
        this.path = path;
    }

    public VersionCollector() {
        this.path = PATH;
    }

    public String getVersionNumber() {
        final Properties properties = new Properties();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = loader.getResourceAsStream(this.path);
        if (stream == null) {
            return "Unable to read the version. Check the file path.";
        }
        try {
            properties.load(stream);
        } catch (final IOException exception) {
            throw new IllegalArgumentException("Unable to read the version from the file: " + this.path + ".",
                  exception);
        }
        return properties.getProperty(VERSION);
    }
}
