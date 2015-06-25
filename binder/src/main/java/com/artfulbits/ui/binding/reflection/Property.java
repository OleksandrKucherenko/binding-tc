package com.artfulbits.ui.binding.reflection;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.reflection.ReflectionUtils.Entry;

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
  /** reference on reflected class entry used for GET operation. */
  private Entry mCachedGet;
  /** reference on reflected class entry used for SET operation. */
  private Entry mCachedSet;

	/* [ CONSTRUCTORS ] ============================================================================================== */

  protected Property(@NonNull final Class<T> type, @NonNull final String name) {
    mType = type;
    mName = name;
  }

  /** Extract generic type information in tricky way. */
  private static <T> Class<T> typeTrick() {
    final Object t = new Trick() {
      Class<T> typeT;
    };

    try {
      final Class<T> type = (Class<T>) t.getClass().getDeclaredFields()[0].getType();

      return type;
    } catch (Throwable ignored) {
      // do nothing
    }

    return null;
  }

  public static <T> Property<T> from(final String name) {
    return new Property<T>((Class<T>) typeTrick(), name);
  }

  @NonNull
  public static Property<Integer> integer(@NonNull final String name) {
    return new Property<>(Integer.class, name);
  }

  @NonNull
  public static Property<Long> number(@NonNull final String name) {
    return new Property<>(Long.class, name);
  }

  @NonNull
  public static Property<Float> decimal4(@NonNull final String name) {
    return new Property<>(Float.class, name);
  }

  @NonNull
  public static Property<Double> decimal(@NonNull final String name) {
    return new Property<>(Double.class, name);
  }

  @NonNull
  public static Property<Character> letter(@NonNull final String name) {
    return new Property<>(Character.class, name);
  }

  @NonNull
  public static Property<Boolean> bit(@NonNull final String name) {
    return new Property<>(Boolean.class, name);
  }

  @NonNull
  public static Property<String> text(@NonNull final String name) {
    return new Property<>(String.class, name);
  }

	/* [ GETTER / SETTER METHODS ] =================================================================================== */

  public final Class<T> getDataType() {
    return mType;
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  private Entry extractGetter(final Object instance) throws NoSuchMethodException {
    Entry result = null;

    final List<Entry> methods = ReflectionUtils.getAll(instance.getClass());

    for (final String prefix : KNOWN_GETTERS) {
      final String name = prefix + mName;
      result = ReflectionUtils.find(methods, name);

      if (null != result) {
        break;
      }
    }

    return result;
  }

  private Entry extractSetter(final Object instance) throws NoSuchMethodException {
    Entry result = null;

    final List<Entry> methods = ReflectionUtils.getAll(instance.getClass());

    for (final String prefix : KNOWN_SETTERS) {
      final String name = prefix + mName;
      result = ReflectionUtils.find(methods, name);

      if (null != result) {
        break;
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public T get(@NonNull final Object instance) {
    try {
      if (null == mCachedGet) {
        mCachedGet = extractGetter(instance);
      }

      return (T) mCachedGet.invoke(instance);
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
      if (null == mCachedSet) {
        mCachedSet = extractSetter(instance);
      }

      mCachedSet.invoke(instance, value);
    } catch (final Throwable ignored) {
      return false;
    }

    return true;
  }

  private interface Trick {
  }
}
