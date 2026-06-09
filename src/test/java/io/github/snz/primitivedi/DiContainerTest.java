package io.github.snz.primitivedi;

import static org.junit.jupiter.api.Assertions.*;

import io.github.snz.primitivedi.exception.DiException;
import io.github.snz.primitivedi.exception.ScopedDiContainerClosedException;
import io.github.snz.primitivedi.stub.*;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class DiContainerTest {
    @Test
    void shouldRegisterAndResolveTransientDependency() {
        DiContainer container =
                DiContainer.builder().transientOf(ServiceA.class).build();

        ServiceA first = container.request(ServiceA.class);
        ServiceA second = container.request(ServiceA.class);

        assertNotSame(first, second);
    }

    @Test
    void shouldRegisterAndResolveScopedDependency() {
        DiContainer container = DiContainer.builder().scopedOf(ServiceA.class).build();

        try (DiContainerScope scope1 = container.createScope()) {
            ServiceA firstInScope1 = scope1.request(ServiceA.class);
            ServiceA secondInScope1 = scope1.request(ServiceA.class);

            assertSame(firstInScope1, secondInScope1);

            try (DiContainerScope scope2 = container.createScope()) {
                ServiceA firstInScope2 = scope2.request(ServiceA.class);

                assertNotSame(firstInScope1, firstInScope2);
            }
        }
    }

    @Test
    void shouldRegisterAndResolveSingletonDependency() {
        DiContainer container =
                DiContainer.builder().singletonOf(ServiceA.class).build();

        ServiceA first = container.request(ServiceA.class);
        ServiceA second = container.request(ServiceA.class);

        assertSame(first, second);
    }

    @Test
    void shouldRegisterAndResolvePreConstructedSingletonInstance() {
        ServiceA expected = new ServiceA();
        DiContainer container =
                DiContainer.builder().singletonOf(ServiceA.class, expected).build();

        ServiceA actual = container.request(ServiceA.class);

        assertSame(expected, actual);
    }

    @Test
    void shouldResolveScopedDependencyAsSingletonWhenRequestedFromRootContainer() {
        DiContainer container = DiContainer.builder().scopedOf(ServiceA.class).build();

        ServiceA first = container.request(ServiceA.class);
        ServiceA second = container.request(ServiceA.class);

        assertSame(first, second);
    }

    @Test
    void shouldDelegateSingletonResolutionToRootContainerWhenRequestedFromScope() {
        DiContainer container = DiContainer.builder()
                .singletonOf(ServiceA.class)
                .build();

        ServiceA rootInstance = container.request(ServiceA.class);

        try (DiContainerScope scope = container.createScope()) {
            ServiceA scopeInstance = scope.request(ServiceA.class);

            assertSame(rootInstance, scopeInstance);
        }
    }

    @Test
    void shouldRegisterUsingSingleParameterOverloads() {
        DiContainer container = DiContainer.builder()
                .transientOf(ServiceA.class)
                .scopedOf(ServiceB.class)
                .singletonOf(ServiceC.class)
                .build();

        container.request(ServiceA.class);
        container.request(ServiceB.class);
        container.request(ServiceC.class);
    }

    @Test
    void shouldRegisterUsingFactoryOverloads() {
        DiContainer container = DiContainer.builder()
                .transientOf(ServiceA.class, c -> new ServiceA())
                .scopedOf(ServiceB.class, c -> new ServiceB(c.request(ServiceA.class)))
                .singletonOf(ServiceC.class, (Function<DiContainerBase, ServiceC>)
                        c -> new ServiceC(c.request(ServiceB.class)))
                .build();

        container.request(ServiceA.class);
        container.request(ServiceB.class);
        container.request(ServiceC.class);
    }

    @Test
    void shouldRegisterUsingInterfaceAndImplementationOverloads() {
        DiContainer container = DiContainer.builder()
                .transientOf(ServiceInterfaceA.class, ServiceA.class)
                .transientOf(ServiceA.class)
                .scopedOf(ServiceInterfaceB.class, ServiceB.class)
                .scopedOf(ServiceB.class)
                .singletonOf(ServiceInterfaceC.class, ServiceC.class)
                .build();

        assertInstanceOf(ServiceA.class, container.request(ServiceInterfaceA.class));
        assertInstanceOf(ServiceB.class, container.request(ServiceInterfaceB.class));
        assertInstanceOf(ServiceC.class, container.request(ServiceInterfaceC.class));
    }

    @Test
    void shouldInstallSingleAndMultipleModules() {
        DiModule moduleA = builder -> builder.transientOf(ServiceA.class);
        DiModule moduleB = builder -> builder.scopedOf(ServiceB.class);
        DiModule moduleC = builder -> builder.singletonOf(ServiceC.class);

        DiContainer container = DiContainer.builder()
                .install(moduleA)
                .install(moduleB, moduleC)
                .build();

        container.request(ServiceA.class);
        container.request(ServiceB.class);
        container.request(ServiceC.class);
    }

    @Test
    void shouldReturnCorrectRegistrationStatus() {
        DiContainer container = DiContainer.builder()
                .transientOf(ServiceA.class)
                .build();

        assertTrue(container.isRegistered(ServiceA.class));
        assertFalse(container.isRegistered(ServiceB.class));
    }

    @Test
    void shouldThrowExceptionWhenDependencyIsNotRegistered() {
        DiContainer container = DiContainer.builder().build();

        assertThrows(DiException.class, () -> {
            container.request(ServiceA.class);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequestingFromClosedScope() {
        DiContainer container = DiContainer.builder()
                .scopedOf(ServiceA.class)
                .build();
        DiContainerScope scope = container.createScope();

        scope.close();

        assertThrows(ScopedDiContainerClosedException.class, () -> {
            scope.request(ServiceA.class);
        });
    }
}
