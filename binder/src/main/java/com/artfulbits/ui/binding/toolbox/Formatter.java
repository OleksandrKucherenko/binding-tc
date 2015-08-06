package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.Formatting;
import com.artfulbits.ui.binding.exceptions.OneWayBindingError;

/** Methods for construction of typical data type converter's. */
@SuppressWarnings({"unused", "unchecked"})
public final class Formatter {
  /* [ CONSTRUCTORS ] ============================================================================================== */

  /** hidden constructor. */
  private Formatter() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS - GENERIC ] ================================================================================== */

  /** No formatting. Input and Output is the same value. */
  @NonNull
  public static <T, V> Formatting<T, V> direct() {
    return new Formatting<T, V>() {
      @Override
      public T toView(V value) {
        return (T) value;
      }

      @Override
      public V toModel(T value) {
        return (V) value;
      }
    };
  }

  /** Reverse formatting instance. */
  @NonNull
  public static <T, V> Formatting<T, V> reverse(@NonNull final Formatting<V, T> f) {
    return new Formatting<T, V>() {
      @Override
      public T toView(final V value) {
        return f.toModel(value);
      }

      @Override
      public V toModel(final T value) {
        return f.toView(value);
      }
    };
  }

  /**
   * Create one way binding - allowed only POP operation, from MODEL to VIEW. VIEW to MODEL - not allowed.
   */
  @NonNull
  public static <T, V> Formatting<T, V> onlyPop(@NonNull final Formatting<T, V> f) {
    return new Formatting<T, V>() {
      @Override
      public T toView(final V value) {
        return f.toView(value);
      }

      @Override
      public V toModel(final T value) {
        throw new OneWayBindingError();
      }
    };
  }

  /**
   * Create one way binding - allowed only POP operation, from MODEL to VIEW. VIEW to MODEL - not allowed.
   */
  @NonNull
  public static <T, V> Formatting<T, V> onlyPop(@NonNull final ToView<T, V> f) {
    return new Formatting<T, V>() {
      @Override
      public T toView(final V value) {
        return f.toView(value);
      }

      @Override
      public V toModel(final T value) {
        throw new OneWayBindingError();
      }
    };
  }

  /**
   * Create one way binding - allowed only PUSH operation, from VIEW to MODEL. MODEL to VIEW - not allowed.
   */
  @NonNull
  public static <T, V> Formatting<T, V> onlyPush(@NonNull final Formatting<T, V> f) {
    return new Formatting<T, V>() {
      @Override
      public T toView(final V value) {
        throw new OneWayBindingError();
      }

      @Override
      public V toModel(final T value) {
        return f.toModel(value);
      }
    };
  }

  /**
   * Create one way binding - allowed only PUSH operation, from VIEW to MODEL. MODEL to VIEW - not allowed.
   */
  @NonNull
  public static <T, V> Formatting<T, V> onlyPush(@NonNull final ToModel<V, T> f) {
    return new Formatting<T, V>() {
      @Override
      public T toView(final V value) {
        throw new OneWayBindingError();
      }

      @Override
      public V toModel(final T value) {
        return f.toModel(value);
      }
    };
  }

  /** Convert String to Number and vise verse. */
  @NonNull
  /* package */ static <T extends Number> Formatting<String, T> toNumber(@NonNull final Class<T> type) {
    return new Formatting<String, T>() {
      @Override
      public String toView(final T value) {
        return value.toString();
      }

      @Override
      public T toModel(final String value) {
        if (Byte.class.equals(type)) {
          return (T) Byte.valueOf(value);
        } else if (Short.class.equals(type)) {
          return (T) Short.valueOf(value);
        } else if (Integer.class.equals(type)) {
          return (T) Integer.valueOf(value);
        } else if (Long.class.equals(type)) {
          return (T) Long.valueOf(value);
        } else if (Float.class.equals(type)) {
          return (T) Float.valueOf(value);
        } else if (Double.class.equals(type)) {
          return (T) Double.valueOf(value);
        }

        throw new AssertionError("Unsupported type. Not implemented yet.");
      }
    };
  }

  /* [ CONCRETE IMPLEMENTATIONS ] ================================================================================== */

  /** String to Integer. */
  @NonNull
  public static Formatting<String, Integer> toInteger() {
    return toNumber(Integer.class);
  }

  @NonNull
  public static Formatting<Integer, Boolean> fromBoolean() {
    return new Formatting<Integer, Boolean>() {
      @Override
      public Boolean toModel(final Integer value) {
        return value > 0;
      }

      @Override
      public Integer toView(final Boolean value) {
        return value ? 1 : 0;
      }
    };
  }

}
