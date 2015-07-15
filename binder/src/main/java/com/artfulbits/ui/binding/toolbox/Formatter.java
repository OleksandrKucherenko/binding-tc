package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.Formatting;

/** Methods for construction of typical data type converter's. */
@SuppressWarnings({"unused", "unchecked"})
public final class Formatter {
  /* [ CONSTRUCTORS ] ============================================================================================== */

  /** hidden constructor. */
  private Formatter() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ============================================================================================ */

  /** No formatting. Input and Output is the same value. */
  @NonNull
  public static <T, V> Formatting<T, V> direct() {
    return new Formatting<T, V>() {
      @Override
      public T toOut(V value) {
        return (T) value;
      }

      @Override
      public V toIn(T value) {
        return (V) value;
      }
    };
  }

  /** Reverse formatting instance. */
  @NonNull
  public static <T, V> Formatting<T, V> reverse(@NonNull final Formatting<V, T> f) {
    return new Formatting<T, V>() {
      @Override
      public T toOut(final V value) {
        return f.toIn(value);
      }

      @Override
      public V toIn(final T value) {
        return f.toOut(value);
      }
    };
  }

  /** Convert String to Number and vise verse. */
  @NonNull
  private static <T extends Number> Formatting<String, T> toNumber(@NonNull final Class<T> type) {
    return new Formatting<String, T>() {
      @Override
      public String toOut(final T value) {
        return value.toString();
      }

      @Override
      public T toIn(final String value) {
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

  /** Convert Number to String. */
  @NonNull
  private static <T extends Number> Formatting<T, String> fromNumber(final Class<T> type) {
    final Formatting<String, T> left;
    return reverse(left = toNumber(type));
  }

  /** String to Integer. */
  @NonNull
  public static Formatting<String, Integer> toInteger() {
    return toNumber(Integer.class);
  }

  /** Integer to String. */
  @NonNull
  public static Formatting<Integer, String> fromInteger() {
    return reverse(toInteger());
  }
}
