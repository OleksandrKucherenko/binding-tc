package com.truecaller.ui.binding.toolbox;

import com.truecaller.ui.binding.Converter;

/** Methods for construction of typical data type converter's. */
public final class Converters {

  /** hidden constructor. */
  private Converters() {
    throw new AssertionError();
  }

  public static <T> Converter<T, T> direct() {
    return new Converter<T, T>() {
      @Override
      public T toOut(T value) {
        return value;
      }

      @Override
      public T toIn(T value) {
        return value;
      }
    };
  }
}
