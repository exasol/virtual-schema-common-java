package com.exasol.adapter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class AdapterFactoryTest {

    @Test
    void testDefaultCloseDoesNotThrow() {
        final AdapterFactory factory = new AdapterFactoryImplementation();
        assertDoesNotThrow(factory::close);
    }

    private final class AdapterFactoryImplementation implements AdapterFactory {
        @Override
        public VirtualSchemaAdapter createAdapter(final AdapterContext context) {
            return null;
        }

        @Override
        public String getAdapterVersion() {
            return "test";
        }

        @Override
        public String getAdapterName() {
            return "test";
        }

        @Override
        public String getAdapterProjectShortTag() {
            return "TEST";
        }
    }
}
