package io.github.snz.primitivedi;

import io.github.snz.primitivedi.exception.DiException;
import java.util.function.Function;

public record DependencyDescriptor<T>(
        Class<T> abstractionType,
        Class<?> implementationType,
        Lifecycle lifecycle,
        Function<AbstractDiContainer, T> generator) {
    public DependencyDescriptor {
        if (generator == null) {
            throwIfTypesNotRelated(abstractionType, implementationType);
        }
    }

    public DependencyDescriptor(
            Class<T> implementationType, Lifecycle lifecycle, Function<AbstractDiContainer, T> generator) {
        this(implementationType, implementationType, lifecycle, generator);
    }

    private void throwIfTypesNotRelated(Class<?> parentType, Class<?> childType) {
        if (!parentType.isAssignableFrom(childType)) {
            throw new DiException(String.format(
                    "%s is not a subclass or equivalent to %s", childType.getName(), parentType.getName()));
        }
    }
}
