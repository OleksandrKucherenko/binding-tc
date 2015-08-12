package com.artfulbits.ui.binding.toolbox;

import android.support.annotation.NonNull;

import com.artfulbits.ui.binding.Binder;
import com.artfulbits.ui.binding.BindingsManager;

import static com.artfulbits.ui.binding.toolbox.Molds.fromCharsToString;

/** Typical binders. */
@SuppressWarnings("unused")
public final class Binders {
  /* [ CONSTRUCTORS ] ============================================================================================= */

  /** hidden constructor. */
  private Binders() {
    throw new AssertionError();
  }

  /* [ TYPED VERSIONS ] =========================================================================================== */

  /** CharSequence-to-CharSequence. */
  @NonNull
  public static Binder<CharSequence, CharSequence> chars(final BindingsManager bm) {
    // used default direct() formatting
    return bm.bind();
  }

  /** CharSequence-to-String. Format {@link Molds#fromCharsToString()} is assigned during the call. */
  @NonNull
  public static Binder<CharSequence, String> strings(final BindingsManager bm) {
    final Binder<CharSequence, String> result = bm.bind();

    // char sequence to string converter assigned
    return result.format(fromCharsToString());
  }

  /** CharSequence-to-Integer. */
  @NonNull
  public static Binder<CharSequence, Integer> numeric(final BindingsManager bm) {
    final Binder<CharSequence, Integer> bind = bm.bind();

    // char sequence to integer converter assigned
    return bind.format(Molds.fromCharsToInteger());
  }

  /** String-to-String. */
  @NonNull
  public static Binder<String, String> texts(final BindingsManager bm) {
    // used default direct() formatting
    return bm.bind();
  }

  /** Integer-to-Integer. */
  @NonNull
  public static Binder<Integer, Integer> integers(final BindingsManager bm) {
    // used default direct() formatting
    return bm.bind();
  }

  /** Double-to-Double */
  @NonNull
  public static Binder<Double, Double> reals(final BindingsManager bm) {
    // used default direct() formatting
    return bm.bind();
  }

  /** Boolean-to-Boolean. */
  @NonNull
  public static Binder<Boolean, Boolean> bools(final BindingsManager bm) {
    // used default direct() formatting
    return bm.bind();
  }

  /** String-to-Integer. */
  @NonNull
  public static Binder<String, Integer> numbers(final BindingsManager bm) {
    final Binder<String, Integer> bind = bm.bind();

    // string to integer converter assigned
    return bind.format(Molds.fromStringToNumber(Integer.class));
  }
}
