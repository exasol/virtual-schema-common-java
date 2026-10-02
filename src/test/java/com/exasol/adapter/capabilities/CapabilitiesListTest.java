package com.exasol.adapter.capabilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Verify that {@code doc/development/api/capabilities_list.md} is up-to-date with enums.
 */
class CapabilitiesListTest {
    private static final Path CAPABILITIES_LIST = Path.of("doc/development/api/capabilities_list.md");
    private static final Pattern LIST_ENTRY = Pattern.compile("^- `([^`]+)`.*$");
    private static final Pattern DEPRECATED_CAPABILITY = Pattern.compile("^\\| `([^`]+)`\\s+\\|.*");

    @Test
    void documentsEveryCapabilityExactlyOnce() throws IOException {
        final List<String> documentedCapabilities = getDocumentedCapabilities();
        final Set<String> expectedCapabilities = getExpectedCapabilities();

        assertEquals(expectedCapabilities.size(), documentedCapabilities.size(),
                "File " + CAPABILITIES_LIST + " must contain every capability exactly once.");
        assertEquals(expectedCapabilities, new LinkedHashSet<>(documentedCapabilities),
                "File " + CAPABILITIES_LIST + " must contain exactly the capabilities exposed by the public enums.");
    }

    private List<String> getDocumentedCapabilities() throws IOException {
        return Files.readAllLines(CAPABILITIES_LIST).stream()
                .flatMap(CapabilitiesListTest::extractCapability)
                .collect(Collectors.toList());
    }

    private static Stream<String> extractCapability(final String line) {
        final Matcher listEntry = LIST_ENTRY.matcher(line);
        if (listEntry.matches()) {
            return Stream.of(listEntry.group(1));
        }
        final Matcher deprecatedCapability = DEPRECATED_CAPABILITY.matcher(line);
        if (deprecatedCapability.matches()) {
            return Stream.of(deprecatedCapability.group(1));
        }
        return Stream.empty();
    }

    private static Set<String> getExpectedCapabilities() {
        final Set<String> capabilities = new LinkedHashSet<>();
        addCapabilities(capabilities, MainCapability.values(), "");
        addCapabilities(capabilities, LiteralCapability.values(), "LITERAL_");
        addCapabilities(capabilities, PredicateCapability.values(), "FN_PRED_");
        addCapabilities(capabilities, ScalarFunctionCapability.values(), "FN_");
        addCapabilities(capabilities, AggregateFunctionCapability.values(), "FN_AGG_");
        return capabilities;
    }

    private static <T extends Enum<T>> void addCapabilities(final Set<String> capabilities, final T[] enumValues, final String prefix) {
        for (final T enumValue : enumValues) {
            capabilities.add(prefix + enumValue.name());
        }
    }
}
