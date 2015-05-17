package com.truecaller.ui.binding.toolbox;

import com.truecaller.ui.binding.Selector;
import com.truecaller.ui.binding.reflection.Property;

import java.util.List;
import java.util.Map;

/** Helpers that simplify common operations with different data storage instances. */
public final class Models {

  private Models() {
    throw new AssertionError();
  }

  /* [ DATA STRUCTURES ] ========================================================================================== */

  public static <I, T> Selector<I, Property<T>> pojo(final I instance, Property<T> selector) {
    return null;
  }

  public static <I, T> Selector<I, Property<T>> map(final Map<String, I> instance, Property<T> selector) {
    return null;
  }

  public static <I, T> Selector<I, Property<T>> index(final List<I> instance, Property<T> selector) {
    return null;
  }

  public static <I, T> Selector<I, Property<T>> index(final I[] instance, Property<T> selector) {
    return null;
  }

  /* [ GENERICS ] ================================================================================================= */

  public static <T> Property<T> property(final String name) {
    return null;
  }

  public static <T> Property<T> name(final String nameGet, final String nameSet) {
    return null;
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  public static Property<String> string(final String name) {
    return null;
  }

  public static Property<String> string(final String nameGet, final String nameSet) {
    return null;
  }

  public static Property<Integer> integer(final String name) {
    return null;
  }

  public static Property<Integer> integer(final String nameGet, final String nameSet) {
    return null;
  }

  public static Property<Long> number(final String name) {
    return null;
  }

  public static Property<Long> number(final String nameGet, final String nameSet) {
    return null;
  }

  public static Property<Boolean> bool(final String name) {
    return null;
  }

  public static Property<Boolean> bool(final String nameGet, final String nameSet) {
    return null;
  }

  public static Property<Double> real(final String name) {
    return null;
  }

  public static Property<Double> real(final String nameGet, final String nameSet) {
    return null;
  }
}
