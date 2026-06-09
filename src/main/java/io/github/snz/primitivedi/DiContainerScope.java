package io.github.snz.primitivedi;

import java.util.HashMap;
import java.util.Map;

public final class DiContainerScope extends DiContainerBase implements AutoCloseable {
    private final DiContainerBase root;
    private final Map<Class<?>, Object> scopeInstances = new HashMap<>();
    private boolean isClosed;

    DiContainerScope(DiContainerBase root) {
        this.root = root;
    }

    @Override
    public <T> T request(Class<T> type) {
        throwIfClosed();
        return super.request(type);
    }

    @Override
    public Map<Class<?>, DependencyDescriptor<?>> getDescriptors() {
        throwIfClosed();
        return root.getDescriptors();
    }

    @Override
    protected Map<Class<?>, Object> getScopeInstances() {
        return scopeInstances;
    }

    @Override
    protected <T> T resolveSingleton(DependencyDescriptor<T> descriptor) {
        return root.resolveSingleton(descriptor);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T resolveScoped(DependencyDescriptor<T> descriptor) {
        if (getScopeInstances().get(descriptor.getAbstractionType()) != null) {
            return (T) getScopeInstances().get(descriptor.getAbstractionType());
        }
        T instance = createInstance(descriptor);
        getScopeInstances().put(descriptor.getAbstractionType(), instance);
        return instance;
    }

    @Override
    public void close() {
        scopeInstances.clear();
        isClosed = true;
    }

    private void throwIfClosed() {
        ThrowHelper.throwIfDiScopeIsClosed(isClosed);
    }
}
