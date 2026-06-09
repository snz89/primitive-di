# primitive-di

a simple di container inspired by [`Microsoft.Extensions.DependencyInjection`](https://www.nuget.org/packages/microsoft.extensions.dependencyinjection)

## Usage

### 1. Define your Services

```java
public interface OrderService {}
public class OrderServiceImpl implements OrderService {}

public class MainController {
    // Dependencies can be requested seamlessly from the container
}
```

### 2. Configure the Container

You can register dependencies explicitly or split them into reusable modules.

```java
import io.github.snz.primitivedi.DiContainer;
import io.github.snz.primitivedi.DiModule;

// Optional: Define a module for better code organization
DiModule repositoriesModule = builder -> {
    builder.transientOf(UserRepository.class, SqlUserRepository.class);
};

DiContainer container = DiContainer.builder()
        // 1. Explicit registrations
        .transientOf(OrderService.class, OrderServiceImpl.class)
        .scopedOf(DatabaseConnection.class)
        
        // 2. Factory / Lambda registrations
        .singletonOf(HttpClient.class, ctx -> HttpClient.newHttpClient())
        
        // 3. Pre-constructed instances
        .singletonOf(AppConfig.class, new AppConfig("production"))
        
        // 4. Install modular configuration
        .install(repositoriesModule)
        .build();
```

### 3. Resolve Dependencies

#### Root Container (Transient & Singleton)

```java
// Transient: creates a new instance every time
OrderService service1 = container.request(OrderService.class);
OrderService service2 = container.request(OrderService.class); // service1 != service2

// Singleton: reuses the same instance across the entire application life
HttpClient client1 = container.request(HttpClient.class);
HttpClient client2 = container.request(HttpClient.class); // client1 == client2
```

#### Scoped Lifecycles

Scoped dependencies are bound to the lifetime of a specific scope. If requested from the root container directly, they gracefully fall back to a singleton behavior.

```java
import io.github.snz.primitivedi.DiContainerScope;

// Create an isolated scope (implements AutoCloseable)
try (DiContainerScope scope1 = container.createScope()) {
    DatabaseConnection conn1 = scope1.request(DatabaseConnection.class);
    DatabaseConnection conn2 = scope1.request(DatabaseConnection.class); // conn1 == conn2 (same scope)
    
    try (DiContainerScope scope2 = container.createScope()) {
        DatabaseConnection conn3 = scope2.request(DatabaseConnection.class); // conn1 != conn3 (different scope)
    }
} // scope1 resources are safely closed here
```
