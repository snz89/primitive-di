package io.github.snz.primitivedi;

import io.github.snz.primitivedi.exception.DiException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDiContainer {
  public abstract Map<Class<?>, DependencyDescriptor<?>> getDescriptors();

  protected abstract Map<Class<?>, Object> getScopeInstances();

  public boolean isRegistered(Class<?> type) {
    return getDescriptors().get(type) != null;
  }

  @SuppressWarnings("unchecked")
  public <T> T request(Class<T> type) {
    var descriptor = (DependencyDescriptor<T>) getDescriptors().get(type);

    if (descriptor == null) {
      throw new DiException(String.format("The %s type is not registered", type.getName()));
    }

    return switch (descriptor.lifecycle()) {
      case SINGLETON -> resolveSingleton(descriptor);
      case SCOPED -> resolveScoped(descriptor);
      case TRANSIENT -> createInstance(descriptor);
    };
  }

  protected abstract <T> T resolveSingleton(DependencyDescriptor<T> descriptor);

  protected abstract <T> T resolveScoped(DependencyDescriptor<T> descriptor);

  protected <T> T createInstance(DependencyDescriptor<T> descriptor) {
    if (descriptor.generator() != null) {
      return descriptor.generator().apply(this);
    }

    Class<?> implType = descriptor.implementationType();
    Constructor<?>[] constructors = implType.getConstructors();

    List<String> unregisteredTypeNames = new ArrayList<>();

    constructorsReview:
    for (Constructor<?> constructor : constructors) {
      Class<?>[] constructorParameterTypes = constructor.getParameterTypes();

      for (Class<?> parameterType : constructorParameterTypes) {
        if (!isRegistered(parameterType)) {
          unregisteredTypeNames.add(parameterType.getSimpleName());
          continue constructorsReview;
        }
      }

      return createWithConstructor(constructor);
    }

    throw new DiException(
        "There was no constructor for which you can get all the parameters. "
            + String.format(
                "Possible necessary types must be registered: %s",
                unregisteredTypeNames.toString()));
  }

  @SuppressWarnings("unchecked")
  private <T> T createWithConstructor(Constructor<?> constructor) {
    List<Object> parameters = new ArrayList<>();
    Class<?>[] constructorParameterTypes = constructor.getParameterTypes();

    for (Class<?> parameterType : constructorParameterTypes) {
      parameters.add(request(parameterType));
    }

    try {
      return (T) constructor.newInstance(parameters.toArray());
    } catch (InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      throw new DiException(
          String.format("Error during creating an %s instance", constructor.getName()), e);
    }
  }
}
