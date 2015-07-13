package com.artfulbits.ui.binding;

/** Implement this interface if you want to know about validation failure. */
public interface Failure {
  void onValidationFailure(final BindingsManager bm, final Binder b);
}
