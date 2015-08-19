package com.artfulbits.binding.toolbox;

/** Low level interface designed for declaring method with one in and out data type. */
public interface ToView<TOut, TIn> {
  /**
   * Convert In- value into Out- value.
   *
   * @param value the value
   * @return the k
   * @throws com.artfulbits.binding.exceptions.OneWayBindingError in case if allowed only one way binding.
   */
  TOut toView(final TIn value);
}