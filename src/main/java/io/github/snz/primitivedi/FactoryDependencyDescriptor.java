package io.github.snz.primitivedi;

import java.util.function.Function;

public final class FactoryDependencyDescriptor<T> implements DependencyDescriptor<T> {
    private final Class<T> abstractionType;
    private final Function<DiContainerBase, T> factory;
    private final Lifecycle lifecycle;

    FactoryDependencyDescriptor(Class<T> abstractionType, Function<DiContainerBase, T> factory, Lifecycle lifecycle) {
        this.abstractionType = abstractionType;
        this.factory = factory;
        this.lifecycle = lifecycle;
    }

    @Override
    public Class<T> getAbstractionType() {
        return abstractionType;
    }

    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public T create(DiContainerBase container) {
        return factory.apply(container);
    }
}
