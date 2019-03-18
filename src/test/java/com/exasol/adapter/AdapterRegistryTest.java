package com.exasol.adapter;

import com.exasol.ExaMetadata;
import com.exasol.adapter.request.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdapterRegistryTest {
    private final AdapterRegistry registry = AdapterRegistry.getInstance();

    @AfterEach
    void afterEach() {
        this.registry.clear();
    }

    @Test
    void testRegistryIsEmptyByDefault() {
        assertThat(this.registry.getRegisteredAdapters(), emptyCollectionOf(VirtualSchemaAdapter.class));
    }

    @Test
    void testRegisterAdapter() {
        final VirtualSchemaAdapter adapter = new DummyAdapter();
        this.registry.registerAdapter("DUMMY", adapter);
        assertThat(this.registry.getRegisteredAdapters(), containsInAnyOrder(adapter));
    }

    @Test
    void testContainsAdapterWithName() {
        final VirtualSchemaAdapter adapter = new DummyAdapter();
        this.registry.registerAdapter("ID", adapter);
        assertThat(this.registry.hasAdapterWithName("ID"), equalTo(true));
    }

    @Test
    void testDoesNotContainAdapterWithName() {
        assertThat(this.registry.hasAdapterWithName("ID"), equalTo(false));
    }

    @Test
    void testGetAdapterForName() {
        final VirtualSchemaAdapter adapter = new DummyAdapter();
        this.registry.registerAdapter("ID", adapter);
        assertThat(this.registry.getAdapterForName("ID"), sameInstance(adapter));
    }

    @Test
    void testGetAdapterForNameThrowsExceptionIfNameIsUnknown() {
        assertThrows(IllegalArgumentException.class, () -> this.registry.getAdapterForName("FooBar"));
    }

    @Test
    void testDescribe() {
        this.registry.registerAdapter("One", null);
        this.registry.registerAdapter("Two", null);
        assertThat(this.registry.describe(), equalTo("Currently registered Virtual Schema Adapters: \"One\", \"Two\""));
    }

    private class DummyAdapter implements VirtualSchemaAdapter {
        @Override
        public void createVirtualSchema(final ExaMetadata metadata, final CreateVirtualSchemaRequest request) {

        }

        @Override
        public void dropVirtualSchema(final ExaMetadata metadata, final DropVirtualSchemaRequest request) {

        }

        @Override
        public void refresh(final ExaMetadata metadata, final RefreshRequest request) {

        }

        @Override
        public void setProperties(final ExaMetadata metadata, final SetPropertiesRequest request) {

        }

        @Override
        public void getCapabilities(final ExaMetadata metadata, final GetCapabilitiesRequest request) {

        }

        @Override
        public void pushdown(final ExaMetadata metadata, final PushdownRequest request) {

        }
    }
}