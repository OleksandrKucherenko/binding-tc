package com.artfulbits.ui.binding.toolbox;

/**
 * Low level interface designed for declaring method with one in and out data type. Primary goal create another method
 * name that will not conflict with {@link ToView} interface.
 */
public interface ToModel<TOut, TIn> {
  /**
   * Convert Out- value into In- value.
   *
   * @param value the value
   * @return the t
   * @throws com.artfulbits.ui.binding.exceptions.OneWayBindingError in case if allowed only one way binding.
   */
  TOut toModel(final TIn value);
}
