package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.ui.binding.Formatting;

/** Methods for construction of typical data type converter's. */
@SuppressWarnings("unused")
public final class Formatter {

  /** hidden constructor. */
  private Formatter() {
    throw new AssertionError();
  }

  /** No formatting. Input and Output is the same value. */
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

  /** Convert String to Number and vise verse. */
  public static <T extends Number> Formatting<String, T> asNumber() {
    return new Formatting<String, T>() {
      @Override
      public String toOut(final Number value) {
        return value.toString();
      }

      @Override
      public T toIn(final String value) {
        return (T) Long.valueOf(value);
      }
    };
  }
}
