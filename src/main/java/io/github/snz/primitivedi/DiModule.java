package io.github.snz.primitivedi;

@FunctionalInterface
public interface DiModule {
    void configure(DiContainerBuilder builder);
}
