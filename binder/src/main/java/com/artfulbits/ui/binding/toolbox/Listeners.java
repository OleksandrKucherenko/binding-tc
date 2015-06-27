package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.Listener;
import com.artfulbits.ui.binding.reflection.Property;

/** Implementation of common listeners. */
@SuppressWarnings("unused")
public final class Listeners {
  /** hidden constructor. */
  private Listeners() {
    throw new AssertionError();
  }

  /**
   * Join multiple listeners into one expression.
   *
   * @param listeners the array of listeners
   * @return the joined listeners instance.
   */
  @NonNull
  public static <T> Listener<T> anyOf(final Listener<T>... listeners) {
    return new Listener<T>() {
      @Override
      public void attach(final Property property, final T instance) {
        for (Listener<T> listener : listeners) {
          listener.attach(property, instance);
        }
      }

      @Override
      public void detach(final Property property, final T instance) {
        for (Listener<T> listener : listeners) {
          listener.detach(property, instance);
        }
      }
    };
  }

  /**
   * Get 'empty listener' instance. this is simply a stub that can be used in expressions but it doing absolutely
   * nothing.
   */
  @NonNull
  public static <T> Listener<T> none() {
    return new Listener<T>() {
      @Override
      public void attach(final Property property, final T instance) {
        // do nothing
      }

      @Override
      public void detach(final Property property, final T instance) {
        // do nothing
      }
    };
  }
}
