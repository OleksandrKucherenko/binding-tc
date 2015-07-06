package com.artfulbits.ui.binding.reflection;

import android.support.annotation.NonNull;

import java.util.List;

/** Class is responsible for accessing a specific abstract 'field' by using reflection. */
@SuppressWarnings("unused")
public class Property<T> {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Use this constant for defining 'empty' name of the getter or setter. */
  public static final String NO_NAME = "";
  /** Array of possible prefixes used for getting the value. */
  private static final String[] KNOWN_GETTERS = new String[]{"get", "has", "is", "exceeds", "m", ""};
  /** Array of possible prefixes used for setting the value. */
  private static final String[] KNOWN_SETTERS = new String[]{"set", "m", ""};

	/* [ MEMBERS ] =================================================================================================== */

  /** Property data type. */
  private final Class<T> mType;
  /** Property name pattern. */
  private final String mName;
  /** reference on reflected class entry used for GET operation. */
  private Entry mCachedGet;
  /** reference on reflected class entry used for SET operation. */
  private Entry mCachedSet;
  /** Name of found 'get' entry. */
  private String mConfirmedGet;
  /** Name of found 'set' entry. */
  private String mConfirmedSet;

	/* [ CONSTRUCTORS ] ============================================================================================== */

  /** Expected automatic 'get' and 'set' finding. */
  public Property(@NonNull final Class<T> type, @NonNull final String name) {
    mType = type;
    mName = name;
  }

  /** 'get' and 'set' are explicitly defined. */
  public Property(@NonNull final Class<T> type, @NonNull final String getName, @NonNull final String setName) {
    mType = type;
    mName = null;

    mConfirmedGet = getName;
    mConfirmedSet = setName;
  }

	/* [ GETTER / SETTER METHODS ] =================================================================================== */

  public final Class<T> getDataType() {
    return mType;
  }

  /** Get resolved 'get' entry name. Null - if not resolved yet. */
  public String getGetterName() {
    return mConfirmedGet;
  }

  /** Get resolved 'set' entry name. Null - if not resolved yet. */
  public String getSetterName() {
    return mConfirmedSet;
  }

  /** Get property value. */
  public T get(@NonNull final Object instance) {
    return get(instance, getterArguments());
  }

  @SuppressWarnings("unchecked")
  public T get(@NonNull final Object instance, final Object... args) {
    try {
      if (null == mCachedGet) {
        mCachedGet = extractGetter(instance);
      }

      return (T) mCachedGet.invoke(instance, args);
    } catch (final Throwable ignored) {
      // TODO: log exception
    }

    return (T) primitiveDefault(mType);
  }

  /** Set property value. */
  public boolean set(@NonNull final Object instance, final T value) {
    try {
      if (null == mCachedSet) {
        mCachedSet = extractSetter(instance);
      }

      mCachedSet.invoke(instance, setterArguments(value));
    } catch (final Throwable ignored) {
      return false;
    }

    return true;
  }

  /* [ OVERRIDES ] ================================================================================================= */

  /** Execution arguments for 'getter'. */
  protected Object[] getterArguments() {
    return (Object[]) null;
  }

  /** Execution arguments for 'setter'. */
  protected Object[] setterArguments(final T value) {
    return new Object[]{value};
  }

  /** Resolve 'getter' entry. */
  protected Entry extractGetter(final Object instance) throws Exception {
    return reflectGetter(instance);
  }

  /** Resolve 'setter' entry. */
  protected Entry extractSetter(final Object instance) throws Exception {
    return reflectSetter(instance);
  }

  /** Extract default value based on class type. */
  protected Object primitiveDefault(@NonNull final Class<T> clazz) {
    if (int.class == clazz || short.class == clazz || long.class == clazz || byte.class == clazz) {
      return (byte) 0;
    }

    if (float.class == clazz || double.class == clazz) {
      return 0.0f;
    }

    if (char.class == clazz) {
      return '\0';
    }

    if (boolean.class == clazz) {
      return false;
    }

    return null;
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  /** find 'getter' using reflection. */
  private Entry reflectGetter(final Object instance) throws NoSuchMethodException {
    Entry result = null;

    final List<Entry> methods = ReflectionUtils.getAll(instance.getClass());

    // explicit name defined
    if (null != mConfirmedGet) {
      result = ReflectionUtils.find(methods, mConfirmedGet);
    }

    // search required
    if (null == result && null != mName) {
      for (final String prefix : KNOWN_GETTERS) {
        final String name = prefix + mName;
        result = ReflectionUtils.find(methods, name);

        if (null != result) {
          mConfirmedGet = name;
          break;
        }
      }
    }

    return result;
  }

  /** find 'setter' using reflection. */
  private Entry reflectSetter(final Object instance) throws NoSuchMethodException {
    Entry result = null;

    final List<Entry> methods = ReflectionUtils.getAll(instance.getClass());

    // explicit name defined
    if (null != mConfirmedSet) {
      result = ReflectionUtils.find(methods, mConfirmedSet);
    }

    // search required
    if (null != result && null != mName) {
      for (final String prefix : KNOWN_SETTERS) {
        final String name = prefix + mName;
        result = ReflectionUtils.find(methods, name);

        if (null != result) {
          mConfirmedSet = name;
          break;
        }
      }
    }

    return result;
  }
}
