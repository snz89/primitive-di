package io.github.snz.primitivedi;

import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public record DependencyDescriptor<T>(
        Class<T> abstractionType,
        @Nullable Class<? extends T> implementationType,
        Lifecycle lifecycle,
        @Nullable Function<AbstractDiContainer, T> generator) {
    public DependencyDescriptor {
        ThrowHelper.throwIfImplTypeAndGeneratorIsMissing(implementationType, generator);
    }

    public DependencyDescriptor(
            Class<T> implementationType, Lifecycle lifecycle, Function<AbstractDiContainer, T> generator) {
        this(implementationType, implementationType, lifecycle, generator);
    }
}
