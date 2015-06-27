package com.artfulbits.ui.binding.toolbox;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingManager;

/** Typical binders. */
public final class Binders {
  /* [ CONSTRUCTORS ] ============================================================================================= */

  /** hidden constructor. */
  private Binders() {
    throw new AssertionError();
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  public static Binder<String, String> texts(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<Integer, Integer> integers(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<Double, Double> reals(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<Boolean, Boolean> bools(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<String, Integer> numeric(final BindingManager bm) {
    return bm.bind();
  }
}
