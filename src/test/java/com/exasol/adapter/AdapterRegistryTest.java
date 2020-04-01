package com.exasol.adapter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AdapterRegistryTest {
    private final AdapterRegistry registry = AdapterRegistry.getInstance();

    @AfterEach
    void afterEach() {
        this.registry.clear();
    }

    @Test
    void testRegistryIsEmptyByDefault() {
        assertThat(this.registry.getRegisteredAdapterFactories(), emptyCollectionOf(AdapterFactory.class));
    }

    @Test
    void testRegisterAdapterFactory() {
        final AdapterFactory factory = new DummyAdapterFactory();
        this.registry.registerAdapterFactory("DUMMY", factory);
        assertThat(this.registry.getRegisteredAdapterFactories(), containsInAnyOrder(factory));
    }

    @Test
    void testContainsAdapterFactoryWithName() {
        final AdapterFactory factory = new DummyAdapterFactory();
        this.registry.registerAdapterFactory("ID", factory);
        assertThat(this.registry.hasAdapterWithName("ID"), equalTo(true));
    }

    @Test
    void testDoesNotContainAdapterFactoryWithName() {
        assertThat(this.registry.hasAdapterWithName("ID"), equalTo(false));
    }

    @Test
    void testLoadFactories() {
        assertThat(this.registry.hasAdapterWithName("DUMMY"), equalTo(false));
    }

    @Test
    void testGetAdapterForName() {
        this.registry.registerAdapterFactory("ID", new DummyAdapterFactory());
        assertThat(this.registry.getAdapterForName("ID"), instanceOf(DummyAdapter.class));
    }

    @Test
    void testGetAdapterForNameThrowsExceptionIfNameIsUnknown() {
        assertThrows(IllegalArgumentException.class, () -> this.registry.getAdapterForName("FooBar"));
    }

    @Test
    void testDescribe() {
        this.registry.registerAdapterFactory("One", null);
        this.registry.registerAdapterFactory("Two", null);
        assertThat(this.registry.describe(),
                equalTo("Currently registered Virtual Schema Adapter factories: \"One\", \"Two\""));
    }

    @Test
    void testDescribeWithNoFactories() {
        assertThat(this.registry.describe(), equalTo("No Virtual Schema Adapter factories are currently reqistered."));
    }
}