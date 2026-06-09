package io.github.snz.primitivedi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class DiContainerBuilder {
    final Map<Class<?>, DependencyDescriptor<?>> dependencyDescriptors = new HashMap<>();
    final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    DiContainerBuilder() {}

    public <T> DiContainerBuilder register(DependencyDescriptor<T> descriptor) {
        dependencyDescriptors.put(descriptor.getAbstractionType(), descriptor);
        return this;
    }

    public <T> DiContainerBuilder transientOf(Class<T> abstractionType, Class<? extends T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(abstractionType, implementationType, Lifecycle.TRANSIENT));
    }

    public <T> DiContainerBuilder transientOf(Class<T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(implementationType, implementationType, Lifecycle.TRANSIENT));
    }

    public <T> DiContainerBuilder transientOf(Class<T> abstractionType, Function<AbstractDiContainer, T> factory) {
        return register(DependencyDescriptor.fromFactory(abstractionType, factory, Lifecycle.TRANSIENT));
    }

    public <T> DiContainerBuilder scopedOf(Class<T> abstractionType, Class<? extends T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(abstractionType, implementationType, Lifecycle.SCOPED));
    }

    public <T> DiContainerBuilder scopedOf(Class<T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(implementationType, implementationType, Lifecycle.SCOPED));
    }

    public <T> DiContainerBuilder scopedOf(Class<T> abstractionType, Function<AbstractDiContainer, T> factory) {
        return register(DependencyDescriptor.fromFactory(abstractionType, factory, Lifecycle.SCOPED));
    }

    public <T> DiContainerBuilder singletonOf(Class<T> abstractionType, Class<? extends T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(abstractionType, implementationType, Lifecycle.SINGLETON));
    }

    public <T> DiContainerBuilder singletonOf(Class<T> implementationType) {
        return register(
                DependencyDescriptor.fromImplementation(implementationType, implementationType, Lifecycle.SINGLETON));
    }

    public <T> DiContainerBuilder singletonOf(Class<T> abstractionType, Function<AbstractDiContainer, T> factory) {
        return register(DependencyDescriptor.fromFactory(abstractionType, factory, Lifecycle.SINGLETON));
    }

    public DiContainer build() {
        return new DiContainer(this);
    }
}
