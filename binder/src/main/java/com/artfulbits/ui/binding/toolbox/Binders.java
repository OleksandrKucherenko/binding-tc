package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingsManager;

/** Typical binders. */
public final class Binders {
  /* [ CONSTRUCTORS ] ============================================================================================= */

  /** hidden constructor. */
  private Binders() {
    throw new AssertionError();
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  public static Binder<String, String> texts(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<CharSequence, CharSequence> chars(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<CharSequence, String> strings(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<Integer, Integer> integers(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<Double, Double> reals(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<Boolean, Boolean> bools(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<String, Integer> numbers(final BindingsManager bm) {
    return bm.bind();
  }

  public static Binder<CharSequence, Integer> numeric(final BindingsManager bm) {
    return bm.bind();
  }
}
