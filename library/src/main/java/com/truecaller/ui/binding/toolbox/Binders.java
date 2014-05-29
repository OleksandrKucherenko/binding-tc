package com.truecaller.ui.binding.toolbox;

import com.truecaller.ui.binding.Binder;
import com.truecaller.ui.binding.BindingManager;

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

  public static Binder<Integer, Integer> numbers(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<Double, Double> floats(final BindingManager bm) {
    return bm.bind();
  }

  public static Binder<Boolean, Boolean> bools(final BindingManager bm) {
    return bm.bind();
  }
}
