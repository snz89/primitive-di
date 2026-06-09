package io.github.snz.primitivedi;

public non-sealed class ImplementationDependencyDescriptor<T> implements DependencyDescriptor<T> {
    private final Class<T> abstractionType;
    private final Class<? extends T> implementationType;
    private final Lifecycle lifecycle;

    ImplementationDependencyDescriptor(
            Class<T> abstractionType, Class<? extends T> implementationType, Lifecycle lifecycle) {
        this.abstractionType = abstractionType;
        this.implementationType = implementationType;
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

    public Class<? extends T> getImplementationType() {
        return implementationType;
    }
}
