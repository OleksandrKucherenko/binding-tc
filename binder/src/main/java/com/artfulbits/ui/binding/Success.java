package com.artfulbits.ui.binding;

/** Implement interface if you want to known about validation success. */
public interface Success {
  void onValidationSuccess(final BindingsManager bm, final Binder b);
}
