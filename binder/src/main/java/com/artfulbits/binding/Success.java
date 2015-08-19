package com.artfulbits.binding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/** Implement interface if you want to known about validation success. */
public interface Success {
  /**
   * Raise 'on validation success' for a specific binder instance..
   *
   * @param bm the manager instance to which binder attached.
   * @param b  the binder instance that raise event
   */
  void onValidationSuccess(@Nullable final BindingsManager bm, @NonNull final Binder<?, ?> b);
}
