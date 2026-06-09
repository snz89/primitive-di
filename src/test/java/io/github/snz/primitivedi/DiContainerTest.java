package io.github.snz.primitivedi;

import io.github.snz.primitivedi.stub.ServiceA;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiContainerTest {
    @Test
    void shouldRegisterAndResolveTransientDependency() {
        DiContainer container = DiContainer.builder()
                .transientOf(ServiceA.class)
                .build();

        ServiceA first = container.request(ServiceA.class);
        ServiceA second = container.request(ServiceA.class);

        assertNotSame(first, second);
    }

    @Test
    void shouldRegisterAndResolveScopedDependency() {
        DiContainer container = DiContainer.builder()
                .scopedOf(ServiceA.class)
                .build();

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
        DiContainer container = DiContainer.builder()
                .singletonOf(ServiceA.class)
                .build();

        ServiceA first = container.request(ServiceA.class);
        ServiceA second = container.request(ServiceA.class);

        assertSame(first, second);
    }

    @Test
    void shouldRegisterAndResolvePreConstructedSingletonInstance() {
        ServiceA expected = new ServiceA();
        DiContainer container = DiContainer.builder()
                .singletonOf(ServiceA.class, expected)
                .build();

        ServiceA actual = container.request(ServiceA.class);

        assertSame(expected, actual);
    }
}