package com.artfulbits.ui.binding.reflection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.artfulbits.ui.binding.exceptions.WrongConfigurationError;
import com.artfulbits.ui.binding.toolbox.Models;

import java.util.List;
import java.util.Locale;

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

  /**
   * Resolve property logic to human readable string.
   * <p/>
   * Example 1: .getText() | setText()<br/> Example 2: .findViewById(...) | &lt;none&gt;()<br/> Example 3: .{Text}(...)
   * | &lt;none&gt;()<br/>
   */
  @Override
  public String toString() {
    final String search = (TextUtils.isEmpty(mName)) ? "<none>" : "{" + mName + "}";
    final String getter = (null == mCachedGet) ? search : mCachedGet.toString();
    final String setter = (null == mCachedSet) ? search : mCachedSet.toString();

    return String.format(Locale.US, "%s | %s", getter, setter);
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

      if (null == mCachedGet) {
        throw new WrongConfigurationError("Cannot resolve GET to real method/field." +
            " Name: " + mName + ", Getter: " + mConfirmedGet);
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

      if (null == mCachedSet) {
        throw new WrongConfigurationError("Cannot resolve SET to real method/field." +
            " Name: " + mName + ", Setter: " + mConfirmedSet);
      }

      mCachedSet.invoke(instance, setterArguments(value));
    } catch (final Throwable ignored) {
      // TODO: log exception
      return false;
    }

    return true;
  }

  /** Resolve 'binding by name' to real instances. Allows to validate configuration. */
  public void resolve(@NonNull final Object instance) throws WrongConfigurationError {
    if (null == mCachedGet) {
      mCachedGet = extractGetter(instance);
    }

    if (null == mCachedSet) {
      mCachedSet = extractSetter(instance);
    }

    // validate results
    if (null == mCachedGet && null == mCachedSet) {
      throw new WrongConfigurationError("Cannot resolve GET and SET to real method(s)/field(s)." +
          " Name: " + mName + ", Getter: " + mConfirmedGet + ", Setter: " + mConfirmedSet);
    }

    if (null == mCachedGet) {
      throw new WrongConfigurationError("Cannot resolve GET to real method/field." +
          " Name: " + mName + ", Getter: " + mConfirmedGet);
    }

    if (null == mCachedSet) {
      throw new WrongConfigurationError("Cannot resolve SET to real method/field." +
          " Name: " + mName + ", Setter: " + mConfirmedSet);
    }
  }

  /* [ OVERRIDES ] ================================================================================================= */

  /** Execution arguments for 'getter'. */
  @Nullable
  protected Object[] getterArguments() {
    return (Object[]) null;
  }

  /** Execution arguments for 'setter'. */
  @NonNull
  protected Object[] setterArguments(final T value) {
    return new Object[]{value};
  }

  /** Resolve 'getter' entry. */
  protected Entry extractGetter(final Object instance) {
    return reflectGetter(instance);
  }

  /** Resolve 'setter' entry. */
  protected Entry extractSetter(final Object instance) {
    return reflectSetter(instance);
  }

  /** Extract default value based on class type. */
  protected Object primitiveDefault(@NonNull final Class<T> clazz) {
    if (byte.class == clazz || Byte.class == clazz) return (byte) 0;
    if (short.class == clazz || Short.class == clazz) return (short) 0;
    if (int.class == clazz || Integer.class == clazz) return 0;
    if (long.class == clazz || Long.class == clazz) return 0L;
    if (float.class == clazz || Float.class == clazz) return 0.0f;
    if (double.class == clazz || Double.class == clazz) return 0.0;
    if (char.class == clazz || Character.class == clazz) return '\0';
    if (boolean.class == clazz || Boolean.class == clazz) return false;

    return null;
  }

	/* [ IMPLEMENTATION & HELPERS ] ================================================================================== */

  /** find 'getter' using reflection. */
  protected Entry reflectGetter(final Object instance) {
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
  protected Entry reflectSetter(final Object instance) {
    Entry result = null;

    final List<Entry> methods = ReflectionUtils.getAll(instance.getClass());

    // explicit name defined
    if (null != mConfirmedSet) {
      result = ReflectionUtils.find(methods, mConfirmedSet);
    }

    // search required
    if (null == result && null != mName) {
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

  /** Compute array of reflection types for setter method. */
  private Class<?>[] setterTypes() {
    final Object[] args = setterArguments(null);
    final Class<?>[] types = new Class<?>[args.length];

    types[0] = Models.<T>typeTrick();

    for (int i = 1; i < args.length; i++) {
      types[i] = args[i].getClass();
    }

    return types;
  }
}
