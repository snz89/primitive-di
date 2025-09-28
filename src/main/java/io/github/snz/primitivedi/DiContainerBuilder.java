package io.github.snz.primitivedi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public final class DiContainerBuilder {
    final Map<Class<?>, DependencyDescriptor<?>> dependencyDescriptors = new HashMap<>();
    final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    DiContainerBuilder() {}

    private <T> DiContainerBuilder addDependency(
            Class<T> abstractionType,
            @Nullable Class<?> implementationType,
            Lifecycle lifecycle,
            @Nullable Function<AbstractDiContainer, T> generator) {
        var descriptor = new DependencyDescriptor<>(abstractionType, implementationType, lifecycle, generator);
        dependencyDescriptors.put(abstractionType, descriptor);
        return this;
    }

    public <T> DiContainerBuilder addTransient(Class<T> abstractionType, Class<?> implementationType) {
        return addDependency(abstractionType, implementationType, Lifecycle.TRANSIENT, null);
    }

    public <T> DiContainerBuilder addTransient(Class<T> implementationType) {
        return addDependency(implementationType, implementationType, Lifecycle.TRANSIENT, null);
    }

    public <T> DiContainerBuilder addTransient(Class<T> abstractionType, Function<AbstractDiContainer, T> generator) {
        return addDependency(abstractionType, null, Lifecycle.TRANSIENT, generator);
    }

    public <T> DiContainerBuilder addScoped(Class<T> abstractionType, Class<?> implementationType) {
        return addDependency(abstractionType, implementationType, Lifecycle.SCOPED, null);
    }

    public <T> DiContainerBuilder addScoped(Class<T> implementationType) {
        return addDependency(implementationType, implementationType, Lifecycle.SCOPED, null);
    }

    public <T> DiContainerBuilder addScoped(Class<T> abstractionType, Function<AbstractDiContainer, T> generator) {
        return addDependency(abstractionType, null, Lifecycle.SCOPED, generator);
    }

    public <T> DiContainerBuilder addSingleton(Class<T> abstractionType, Class<?> implementationType) {
        return addDependency(abstractionType, implementationType, Lifecycle.SINGLETON, null);
    }

    public <T> DiContainerBuilder addSingleton(Class<T> implementationType) {
        return addDependency(implementationType, implementationType, Lifecycle.SINGLETON, null);
    }

    public <T> DiContainerBuilder addSingleton(Class<T> abstractionType, Function<AbstractDiContainer, T> generator) {
        return addDependency(abstractionType, null, Lifecycle.SINGLETON, generator);
    }

    public DiContainer build() {
        return new DiContainer(this);
    }
}
