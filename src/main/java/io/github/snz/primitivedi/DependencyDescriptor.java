package io.github.snz.primitivedi;

import java.util.function.Function;

public sealed interface DependencyDescriptor<T>
        permits ImplementationDependencyDescriptor, FactoryDependencyDescriptor {
    Class<T> getAbstractionType();

    Lifecycle getLifecycle();

    public static <T> DependencyDescriptor<T> fromImplementation(
            Class<T> abstractionType, Class<? extends T> implementationType, Lifecycle lifecycle) {
        return new ImplementationDependencyDescriptor<>(abstractionType, implementationType, lifecycle);
    }

    public static <T> DependencyDescriptor<T> fromFactory(
            Class<T> abstractionType, Function<AbstractDiContainer, T> factory, Lifecycle lifecycle) {
        return new FactoryDependencyDescriptor<>(abstractionType, factory, lifecycle);
    }
}
