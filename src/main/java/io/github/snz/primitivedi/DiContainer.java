package io.github.snz.primitivedi;

import java.util.Collections;
import java.util.Map;

public final class DiContainer extends DiContainerBase {
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

    public DiContainerScope createScope() {
        return new DiContainerScope(this);
    }

    @Override
    protected Map<Class<?>, Object> getScopeInstances() {
        return scopeInstances;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T resolveSingleton(DependencyDescriptor<T> descriptor) {
        if (getScopeInstances().get(descriptor.getAbstractionType()) != null) {
            return (T) getScopeInstances().get(descriptor.getAbstractionType());
        }
        T instance = createInstance(descriptor);
        getScopeInstances().put(descriptor.getAbstractionType(), instance);
        return instance;
    }

    @Override
    protected <T> T resolveScoped(DependencyDescriptor<T> descriptor) {
        return resolveSingleton(descriptor);
    }
}
