package io.github.snz.primitivedi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DiContainer extends AbstractDiContainer {
    private final Map<Class<?>, DependencyDescriptor<?>> descriptors;
    private final Map<Class<?>, Object> scopeInstances;

    DiContainer(DiContainerBuilder builder) {
        descriptors = builder.dependencyDescriptors;
        scopeInstances = builder.singletonInstances;
    }

    public static DiContainerBuilder builder() {
        return new DiContainerBuilder();
    }

    @Override
    public Map<Class<?>, DependencyDescriptor<?>> getDescriptors() {
        return Collections.unmodifiableMap(descriptors);
    }

    public ScopedDiContainer createScope() {
        return new ScopedDiContainer(this);
    }

    @Override
    protected Map<Class<?>, Object> getScopeInstances() {
        return scopeInstances;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T resolveSingleton(DependencyDescriptor<T> descriptor) {
        if (getScopeInstances().get(descriptor.abstractionType()) != null) {
            return (T) getScopeInstances().get(descriptor.abstractionType());
        }
        T instance = createInstance(descriptor);
        getScopeInstances().put(descriptor.abstractionType(), instance);
        return instance;
    }

    @Override
    protected <T> T resolveScoped(DependencyDescriptor<T> descriptor) {
        return resolveSingleton(descriptor);
    }
}
