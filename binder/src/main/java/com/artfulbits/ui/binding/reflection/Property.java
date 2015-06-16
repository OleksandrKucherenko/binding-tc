package com.artfulbits.ui.binding.reflection;

import android.support.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.List;

/** Class is responsible for accessing a specific abstract 'field' by using reflection. */
public class Property<T> {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Array of possible prefixes used for getting the value. */
  private static final String[] KNOWN_GETTERS = new String[]{"get", "has", "is", "exceeds", ""};
  /** Array of possible prefixes used for setting the value. */
  private static final String[] KNOWN_SETTERS = new String[]{"set", ""};

	/* [ MEMBERS ] =================================================================================================== */	

  /** Property data type. */
  private final Class<T> mType;
  /** Property name pattern. */
  private final String mName;

	/* [ CONSTRUCTORS ] ============================================================================================== */	

  protected Property(final Class<T> type, final String name) {
    mType = type;
    mName = name;
  }

	/* [ GETTER / SETTER METHODS ] =================================================================================== */	

  public final Class<T> getDataType() {
    return mType;
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */	

  private Method extractGetter(final Object instance) throws NoSuchMethodException {
    Method result = null;

    final List<Method> methods = ReflectionUtils.getAllMethods(instance.getClass());

    for (final String prefix : KNOWN_GETTERS) {
      final String name = prefix + mName;
      result = ReflectionUtils.findMethod(methods, name);

      if (null != result) {
        break;
      }
    }

    return result;
  }

  private Method extractSetter(final Object instance) throws NoSuchMethodException {
    Method result = null;

    final List<Method> methods = ReflectionUtils.getAllMethods(instance.getClass());

    for (final String prefix : KNOWN_SETTERS) {
      final String name = prefix + mName;
      result = ReflectionUtils.findMethod(methods, name);

      if (null != result) {
        break;
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public T get(@NonNull final Object instance) {
    try {
      return (T) extractGetter(instance).invoke(instance);
    } catch (final Throwable ignored) {
      // TODO: log exception
    }

    return (T) primitiveDefault(mType);
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

  public boolean set(@NonNull final Object instance, final T value) {
    try {
      extractSetter(instance).invoke(instance, value);
    } catch (final Throwable ignored) {
      return false;
    }

    return true;
  }
}
