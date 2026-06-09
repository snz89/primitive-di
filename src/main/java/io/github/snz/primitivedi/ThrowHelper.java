package io.github.snz.primitivedi;

import io.github.snz.primitivedi.exception.DiException;
import io.github.snz.primitivedi.exception.ScopedDiContainerClosedException;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

class ThrowHelper {
    static void throwIfDescriptorIsNull(@Nullable DependencyDescriptor<?> descriptor, Class<?> type) {
        if (descriptor == null) {
            throw new DiException(String.format("Descriptor for %s is not registered", type.getName()));
        }
    }

    static <T> void throwIfImplTypeAndGeneratorIsMissing(
            @Nullable Class<?> implementationType, @Nullable Function<DiContainerBase, T> generator) {
        if (implementationType == null && generator == null) {
            throw new DiException("No generator or implementation type is provided.");
        }
    }

    static void throwIfDiScopeIsClosed(boolean isClosed) {
        if (isClosed) {
            throw new ScopedDiContainerClosedException("Trying to use a closed di scope.");
        }
    }

    private ThrowHelper() {}
}
