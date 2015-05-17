package com.truecaller.ui.binding.reflection;

import java.lang.reflect.Method;

/** Class is responsible for accessing a specific abstract 'field' by using reflection. */
public class Property<T> {
  /** Array of possible prefixes. */
  private static final String[] KNOWN_GETTERS = new String[]{"get", "has", "is", "exceeds", ""};

  /** Property data type. */
  private final Class<T> mType;
  /** Property name pattern. */
  private final String mName;

  protected Property(final Class<T> type, final String name) {
    mType = type;
    mName = name;
  }

  public final Class<T> getDataType() {
    return mType;
  }

  private Method extractGetter(final Object instance) throws NoSuchMethodException {
    Method result = null;

    for (String prefix : KNOWN_GETTERS) {
      result = instance.getClass().getMethod(prefix + mName);

      if (null != result) {
        break;
      }
    }

    return result;
  }

  private Method extractSetter(final Object instance) throws NoSuchMethodException {
    return instance.getClass().getMethod(mName, mType);
  }

  @SuppressWarnings("unchecked")
  public T get(final Object instance) {
    try {
      return (T) extractGetter(instance).invoke(instance);
    } catch (final Throwable ignored) {
      // TODO: log exception
    }

    return (T) primitiveDefault(mType);
  }

  public boolean set(final Object instance, final T value) {

    try {
      extractSetter(instance).invoke(instance, value);
    } catch (final Throwable ignored) {
      return false;
    }

    return true;
  }

  protected Object primitiveDefault(final Class<T> clazz) {
    if (int.class == clazz || short.class == clazz || long.class == clazz || byte.class == clazz) {
      return (byte) 0;
    }

    if (float.class == clazz || double.class == clazz) {
      return 0.0f;
    }

    if (char.class == clazz) {
      return '\0';
    }

    return false;
  }
}
