package io.github.snz.primitivedi;

import io.github.snz.primitivedi.exception.ScopedDiContainerClosedException;
import java.util.HashMap;
import java.util.Map;

public final class ScopedDiContainer extends AbstractDiContainer implements AutoCloseable {
    private AbstractDiContainer root;
    private final Map<Class<?>, Object> scopeInstances = new HashMap<>();
    private boolean isClosed;

    ScopedDiContainer(AbstractDiContainer root) {
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
        if (getScopeInstances().get(descriptor.abstractionType()) != null) {
            return (T) getScopeInstances().get(descriptor.abstractionType());
        }
        T instance = createInstance(descriptor);
        getScopeInstances().put(descriptor.abstractionType(), instance);
        return instance;
    }

    @Override
    public void close() {
        scopeInstances.clear();
        root = null;
        isClosed = true;
    }

    private void throwIfClosed() {
        if (isClosed) {
            throw new ScopedDiContainerClosedException("Trying to use a scoped di container area outside the scope");
        }
    }
}
