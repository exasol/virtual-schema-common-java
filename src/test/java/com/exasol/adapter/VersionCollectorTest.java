package com.exasol.adapter;

import com.exasol.logging.VersionCollector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
}