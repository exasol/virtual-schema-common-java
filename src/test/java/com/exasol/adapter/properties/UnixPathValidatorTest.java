package com.exasol.adapter.properties;

import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

// Since the production code only runs on a Unix-like OS, we run the unit test
// only on OSes that use Unix filesystem conventions.
@EnabledOnOs({ OS.LINUX, OS.FREEBSD, OS.OPENBSD, OS.MAC, OS.SOLARIS, OS.AIX })
class UnixPathValidatorTest extends AbstractPropertyValidatorTest {
    // [utest -> dsn~validating-unix-paths~1]
    @ValueSource(strings = {
            "/a", //
            "/c.conf", //
            "/.foo", //
            "/a/b/c.conf", //
            "/a/b/c.conf", //
            "/a/b/.foo", //
    })
    @ParameterizedTest
    void testWhenGivenProperUnixPathThenValidationIsSuccessful(final String path) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_PATH", path);
        final UnixPathValidator validator = factory.absolutePath("THE_PATH");
        final ValidationResult result = validator.validate();
        assertThat(result.isValid(), equalTo(true));
    }

    // [utest -> dsn~validating-unix-paths~1]
    // [utest -> dsn~only-absolute-paths-are-valid-in-properties~1]
    @ValueSource(strings = { //
            "relative/path", //
            "///", //
            "    ", //
            "./invalid.path", //
            "../invalid.path", //
            "/invalid.path/.", //
            "/invalid.path/..", //
            "/sorry/../no/", //
            "/does/./not/work", //
            "/this//also/does/not", //
            "/foo%2E%2E%2Fbar", //
            "/foo\tbar", //
            "/foo\nbar", //
            "/foo\rbar", //
            "/foo\\bar", //
            "/foo\u0000bar", //
            "/foo/.\u0000./bar", //
            "/\u0000.\u0000./bar", //
            "/foo/\u002E./bar", //
            "/foo/\u002E\u002E/bar", //
            "http://foo/bar", //
            "ftp://foo/bar", //
            "file://foo/bar", //
            "/end/with/dot/." //
    })
    @ParameterizedTest
    void testWhenGivenInvalidUnixPathThenValidationFails(final String path) {
        final ValidatorFactory factory = createValidatorFactoryWithProperties("THE_PATH", path);
        final UnixPathValidator validator = factory.absolutePath("THE_PATH");
        final ValidationResult result = validator.validate();
        assertAll(
                () -> assertThat(result.isValid(), equalTo(false)),
                () -> assertThat(result.getMessage(),
                        equalTo("E-VSCOMJAVA-59: The property 'THE_PATH' contains an invalid path: '"
                                + path +
                                "'. Known mitigations:\n"
                                + "* Make sure you provide an absolute path.\n"
                                + "* Don't use a protocol specifier (like 'http:').\n"
                                + "* Avoid blank paths, tabs, newlines and carriage returns.\n"
                                + "* Please remove any kind of path traversal characters ('.', '..', '//').")));
    }
}
