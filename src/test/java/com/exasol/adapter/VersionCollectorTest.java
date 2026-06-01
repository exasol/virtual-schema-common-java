package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.exasol.logging.VersionCollector;

class VersionCollectorTest {
    @Test
    void testGetVesionNumber() {
        final VersionCollector versionCollector = new VersionCollector("pom.properties");
        final String versionNumber = versionCollector.getVersionNumber();
        assertThat(versionNumber, equalTo("1.2.3"));
    }

    @Test
    void testGetVesionNumberInvalidPath() {
        final VersionCollector versionCollector = new VersionCollector("blablapom.properties");
        final String versionNumber = versionCollector.getVersionNumber();
        assertThat(versionNumber, equalTo("UNKNOWN"));
    }

    @Test
    void testGetVersionNumberClosesResourceStream() {
        final TrackingInputStream stream = new TrackingInputStream("version=7.8.9");
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new ClassLoader(originalClassLoader) {
            @Override
            public InputStream getResourceAsStream(final String name) {
                if ("version.properties".equals(name)) {
                    return stream;
                }
                return super.getResourceAsStream(name);
            }
        });
        try {
            final VersionCollector versionCollector = new VersionCollector("version.properties");
            assertThat(versionCollector.getVersionNumber(), equalTo("7.8.9"));
            assertThat(stream.closed, equalTo(true));
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    @Test
    void testGetVersionNumberFallsBackWhenContextClassLoaderMissing() {
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(null);
        try {
            final VersionCollector versionCollector = new VersionCollector("pom.properties");
            assertThat(versionCollector.getVersionNumber(), equalTo("1.2.3"));
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    @Test
    void testGetVersionNumberWithoutVersionProperty() {
        final VersionCollector versionCollector = new VersionCollector("version-without-key.properties");
        assertThat(versionCollector.getVersionNumber(), equalTo("UNKNOWN"));
    }

    @Test
    void testGetVersionNumberWithMissingFile() {
        final VersionCollector versionCollector = new VersionCollector("missing.properties");
        assertThat(versionCollector.getVersionNumber(), equalTo("UNKNOWN"));
    }

    private static final class TrackingInputStream extends ByteArrayInputStream {
        private boolean closed;

        private TrackingInputStream(final String content) {
            super(content.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void close() throws IOException {
            this.closed = true;
            super.close();
        }
    }
}
