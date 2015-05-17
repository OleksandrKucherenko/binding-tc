package com.truecaller.ui.binding.toolbox;

import com.truecaller.ui.binding.Converter;

/** Methods for construction of typical data type converter's. */
public final class Converters {

  /** hidden constructor. */
  private Converters() {
    throw new AssertionError();
  }

  public static <T, V extends T> Converter<T, V> direct() {
    return new Converter<T, V>() {
      @Override
      public T toOut(V value) {
        return value;
      }

      @Override
      public V toIn(T value) {
        return (V) value;
      }
    };
  }

  public static <T extends Number> Converter<String, T> asNumber() {
    return new Converter<String, T>() {
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
