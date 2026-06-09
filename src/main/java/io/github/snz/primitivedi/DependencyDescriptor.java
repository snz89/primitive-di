package io.github.snz.primitivedi;

import io.github.snz.primitivedi.exception.DiException;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public record DependencyDescriptor<T>(
        Class<T> abstractionType,
        @Nullable Class<? extends T> implementationType,
        Lifecycle lifecycle,
        @Nullable Function<AbstractDiContainer, T> generator) {
    public DependencyDescriptor {
        if (generator == null && implementationType == null) {
            throw new DiException("No generator or implementation type is provided.");
        }
    }

    public DependencyDescriptor(
            Class<T> implementationType, Lifecycle lifecycle, Function<AbstractDiContainer, T> generator) {
        this(implementationType, implementationType, lifecycle, generator);
    }
}
